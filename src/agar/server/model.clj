(ns agar.server.model
  (:require
    [agar.constants :as constants]
    [clojure.string :as string]
    [clojure.set :as set]
    )
  )

; Remote state

(defonce remote
  (ref {
    :user-counter 0
    :users {}
    })
  )

; User management

(defn default-user
  [uid] {
    :name (str "Anonymous " uid)
    :position {:x 0.0 :y 0.0}
    :velocity constants/initial-velocity
    :radius 10
  })

(defn next-uid
  [_]
  (dosync (alter remote #(update % :user-counter inc)))
  (@remote :user-counter)
  )

(defn add-user
  [uid]
  (dosync
    (alter
      remote
      #(assoc-in % [:users uid] (default-user uid))
      )
    )
  )

(defn remove-user
  [uid]
  (dosync
    (alter
      remote
      #(update-in % [:users] dissoc uid)
      )
    )
  )

(defn move-user
  [m uid {:keys [position velocity] :as user}]
  (let [
    delta constants/tick-interval
    x (:x position) y (:y position)
    vx (:x velocity) vy (:y velocity)
    dx (* delta vx) dy (* delta vy)
    x-prime (+ x dx) y-prime (+ y dy)
    position-prime {:x x-prime :y y-prime}
    ]
    (assoc m uid (assoc user :position position-prime))
    )
  )

; Remote transitions
(defn move-users
  [remote]
  (update remote :users #(reduce-kv move-user {} %))
  )

(defn update-remote
  [remote]
  (-> remote
    move-users
    )
  )

(defn tick
  []
  (dosync (alter remote update-remote))
  )

(defn username
  [uid username]
  (dosync (alter remote #(assoc-in % [:users uid] username)))
  )

(defn reverse-username
  [uid username]
  (dosync (alter remote assoc :username (string/reverse username)))
  )
