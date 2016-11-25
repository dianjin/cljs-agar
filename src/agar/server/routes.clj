(ns agar.server.routes
  (:require
    [agar.constants :as constants]
    [agar.server.model :as model]
    [agar.server.update :as update]
    [ring.middleware.defaults :as defaults]
    [ring.middleware.reload :as reload]
    [ring.middleware.cors :as cors]
    [ring.util.response :as response]
    [environ.core :as environ]
    [taoensso.sente :as sente]
    [taoensso.sente.server-adapters.http-kit :as http-kit]
    [compojure.core :refer [defroutes GET POST]]
    [compojure.route :as route]))

(declare channel-socket)

(defn start-websocket []
  (defonce channel-socket
    (sente/make-channel-socket!
      http-kit/sente-web-server-adapter
      {:user-id-fn #'update/next-uid}
      )
    )
  )

(defroutes routes
  (GET "/" req
    (response/content-type
      (response/resource-response "public/index.html")
      "text/html"
      )
    )
  (GET "/status" req
    (str "Running: " (pr-str @(:connected-uids channel-socket))))
  (GET "/chsk" req
    ((:ajax-get-or-ws-handshake-fn channel-socket)
      req
      )
    )
  (POST "/chsk" req
    ((:ajax-post-fn channel-socket)
      req
      )
    )
  (route/resources "/")
  (route/not-found "Not found")
  )

(def handler
  (-> #'routes
    (cond-> (environ/env :dev?) (reload/wrap-reload))
    (defaults/wrap-defaults
      (assoc-in defaults/site-defaults [:security :anti-forgery] false)
      )
    (cors/wrap-cors :access-control-allow-origin [#".*"]
                    :access-control-allow-methods [:get :put :post :delete]
                    :access-control-allow-credentials ["true"])
    )
  )

(defmulti event :id)
(defmethod event :chsk/ws-ping [_])
(defmethod event :default
  [{:as ev-msg :keys [event]}]
  (println "Unhandled event: " event)
  )

; Broadcaster

(defn broadcast
  []
  (doseq
    [uid (:any @(:connected-uids channel-socket))]
    ((:send-fn channel-socket)
      uid
      [:agar/remote @model/remote]
      )
    )
  )

; Ticker

(defn ticker
  []
  (while true
    (Thread/sleep constants/tick-interval)
    (try
      (update/tick)
      (broadcast)
      (catch Exception ex (println ex))
      )
    )
  )

(defn start-ticker
  []
  (defonce ticker-thread
    (doto (Thread. ticker)
      (.start)
      )
    )
  )

; Connecting / disconnecting

(defmethod event :chsk/uidport-open
  [{:keys [uid client-id]}]
  (println "New connection:" uid client-id)
  (update/add-player uid)
  )

(defmethod event :chsk/uidport-close
  [{:keys [uid]}]
  (println "Disconnected:" uid)
  (update/remove-player uid)
  )

; User triggered transitions

(defmethod event :agar/start-play
  [{:keys [uid]}]
  (update/start-play uid)
  )

(defmethod event :agar/set-mouse-position
  [{:keys [uid ?data]}]
  (update/set-mouse-position uid ?data)
  )

; Router

(defn start-router
  []
  (defonce router
    (sente/start-chsk-router! (:ch-recv channel-socket) event)
    )
  )
