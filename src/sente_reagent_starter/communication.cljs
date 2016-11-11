(ns sente_reagent_starter.communication
  (:require
    [sente_reagent_starter.ainit]
    [sente_reagent_starter.model :as model]
    [sente_reagent_starter.config :as config]
    [taoensso.sente :as sente]))

(defn get-chsk-url
  "Connect to a configured server instead of the page host"
  [protocol chsk-host chsk-path type]
  (let [protocol (case type :ajax protocol
                            :ws   (if (= protocol "https:") "wss:" "ws:"))]
    (str protocol "//" config/server chsk-path)))

(defonce channel-socket
  (with-redefs [sente/get-chsk-url get-chsk-url]
    (sente/make-channel-socket! "/chsk" {:type :auto})
    )
  )

(defonce chsk (:chsk channel-socket))
(defonce ch-chsk (:ch-recv channel-socket))
(defonce chsk-send! (:send-fn channel-socket))
(defonce chsk-state (:state channel-socket))

(defmulti event-msg-handler :id)

(defmethod event-msg-handler :default
  [{:as ev-msg :keys [event]}]
  (println "Unhandled event: %s" event)
  )

; Messages from server
(defmethod event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (if (= ?data {:first-open? true})
    (println "Channel socket successfully established!")
    (println "Channel socket state change:" ?data)
    )
  )

(defmethod event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  (model/remote! (second ?data))
  )

; Messages to server
(defn username
  []
  (chsk-send! [:sente_reagent_starter/username (:username @model/my-state)]))

(defmethod event-msg-handler :chsk/handshake
  [{:as ev-msg :keys [?data]}]
  (let [[?uid ?csrf-token ?handshake-data] ?data]
    (println "Handshake:" ?data)
    (model/uid! ?uid)
    )
  )

(defonce router
  (sente/start-client-chsk-router! ch-chsk event-msg-handler)
  )
