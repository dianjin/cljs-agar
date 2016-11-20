(ns agar.server.model
  (:require
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

(defn remove-user [uid]
  (dosync
    (alter
      remote
      #(update-in % [:users] dissoc uid)
      )
    )
  )

; Remote transitions

(defn tick
  []
  (dosync (alter remote identity))
  )

(defn username
  [uid username]
  (dosync (alter remote #(assoc-in % [:users uid] username)))
  )

(defn reverse-username
  [uid username]
  (dosync (alter remote assoc :username (string/reverse username)))
  )
