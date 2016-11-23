(ns agar.server.model
  (:require
    [agar.server.body :as body]
    )
  )

; Remote state

(defonce remote
  (ref {
    :player-counter 0
    :players {}
    :edibles (body/initial-edibles)
    })
  )

; Remote updaters

(defn move-players
  [remote]
  (update
    remote
    :players
    #(reduce-kv body/move-player {} %)
    )
  )

(defn set-mouse-position
  [uid mouse-from-origin remote]
  (update-in
    remote
    [:players uid]
    #(body/steer-player mouse-from-origin %)
    )
  )

(defn add-player
  [uid remote]
  (assoc-in
    remote
    [:players uid]
    (body/default-player uid)
    )
  )
