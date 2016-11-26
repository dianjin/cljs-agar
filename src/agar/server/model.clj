(ns agar.server.model
  (:require
    [agar.server.player :as player]
    [agar.physics :as physics]
    )
  )

; Remote state

(defonce remote
  (ref {
    :player-counter 0
    :players (player/initial-edibles)
    })
  )

; Remote updaters

(defn eat-players
  [{:keys [players] :as remote}]
  (let [
    edible-players (filter (fn [[_ p]] (player/edible? p)) players)
    pairs (physics/overlapping-pairs edible-players [])
    ]
    (update
      remote
      :players
      #(-> %
        ((partial player/update-eatens (map first pairs)))
        ((partial player/update-eaters (map second pairs)))
        )
      )
    )
  )

(defn steer-cpus
  [remote]
  (update
    remote
    :players
    #(player/steer-cpus %)
    )
  )

(defn add-cpu
  [{:keys [players player-counter] :as remote}]
  (assoc
    remote
    :players
    (merge players {(inc player-counter) (player/type->player :cpu)})
    :player-counter
    (inc player-counter)
    )
  )

(defn move-players
  [remote]
  (update
    remote
    :players
    #(player/move-players %)
    )
  )

(defn set-mouse-position
  [uid pos-from-origin remote]
  (update-in
    remote
    [:players uid]
    (fn [{:keys [alive] :as player}]
      (if (not alive)
        player
        (player/steer-player-towards pos-from-origin player)
        )
      )
    )
  )

(defn add-player
  [uid remote]
  (assoc-in
    remote
    [:players uid]
    (player/type->player :user)
    )
  )
