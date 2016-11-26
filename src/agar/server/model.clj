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
    edible-players (filter (fn [[_ p]] (body/edible? p)) players)
    pairs (physics/overlapping-pairs edible-players [])
    ]
    (update
      remote
      :players
      #(-> %
        ((partial body/update-eatens (map first pairs)))
        ((partial body/update-eaters (map second pairs)))
        )
      )
    )
  )

(defn steer-players
  [remote]
  (update
    remote
    :players
    #(reduce-kv
      (fn [m uid player]
        (assoc m uid (body/steer-player % player))
        )
      {}
      %
      )
    )
  )

(defn add-cpu
  [{:keys [players player-counter] :as remote}]
  (assoc
    remote
    :players
    (merge
      players
      {(inc player-counter) (body/type->player :cpu)}
      )
    :player-counter
    (inc player-counter)
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
    #(body/steer-player-towards mouse-from-origin %)
    )
  )

(defn add-player
  [uid remote]
  (assoc-in
    remote
    [:players uid]
    (body/type->player :player)
    )
  )
