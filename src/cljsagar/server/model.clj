(ns cljsagar.server.model
  (:require
    [cljsagar.server.player :as player]
    [cljsagar.physics :as physics]
    )
  )

; ~~~~~~~~~~~~~~~~~~~~~~~~
; State
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defonce remote
  (ref {
    :player-counter 0
    :players (player/initial-edibles)
    })
  )

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Updaters
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn steer-cpus
  [remote]
  (update remote :players player/steer-cpus)
  )

(defn move-players
  [remote]
  (update remote :players #(player/move-players %))
  )

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

(defn steer-user
  [uid pos-from-origin remote]
  (update-in
    remote
    [:players uid]
    (fn [{:keys [alive] :as player}]
      (if alive
        (player/steer-player-towards pos-from-origin player)
        player
        )
      )
    )
  )

(defn add-cpu
  [uid {:keys [players player-counter] :as remote}]
  (let [
    new-id (inc player-counter)
    new-cpu (assoc (player/type->player new-id :cpu) :creator uid)
    ]
    (assoc
      remote
      :players (merge players {new-id new-cpu})
      :player-counter new-id
      )
    )
  )

(defn add-player
  [uid remote]
  (assoc-in remote [:players uid] (player/type->player uid :user))
  )

(defn remove-player
  [uid remote]
  (update remote :players (partial player/remove-players uid))
  )
