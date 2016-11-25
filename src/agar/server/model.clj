(ns agar.server.model
  (:require
    [agar.server.body :as body]
    [agar.physics :as physics]
    )
  )

; Remote state

(defonce remote
  (ref {
    :player-counter 0
    :players (body/initial-edibles)
    })
  )

; Remote updaters

(defn eat-players
  [{:keys [players] :as remote}]
  (let [
    alive-players (filter (fn [[_ {a :alive}]] a) players)
    pairs (physics/overlapping-pairs alive-players [])
    ]
    (update
      remote
      :players
      #(-> %
        ((partial body/remove-eatens (map first pairs)))
        ((partial body/augment-eaters (map second pairs)))
        )
      )
    )
  )

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
