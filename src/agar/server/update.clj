(ns agar.server.update
  (:require
    [agar.server.model :as model]
    )
  )

(defn next-uid
  [_]
  (dosync
    (alter model/remote #(update % :player-counter inc))
    )
  (@model/remote :player-counter)
  )

(defn add-player
  [uid]
  (dosync
    (alter model/remote (partial model/add-player uid))
    )
  )

(defn remove-player
  [uid]
  (dosync
    (alter model/remote (partial model/remove-player uid))
    )
  )

(defn start-play
  [uid]
  (dosync
    (alter model/remote #(assoc-in % [:players uid :alive] true))
    )
  )

(defn add-cpu
  [uid]
  (dosync
    (alter model/remote (partial model/add-cpu uid))
    )
  )

(defn steer-user
  [uid position]
  (dosync
    (alter model/remote (partial model/steer-user uid position))
    )
  )

(defn tick
  []
  (dosync
    (alter
      model/remote
      #(-> %
        model/steer-cpus
        model/move-players
        model/eat-players
        )
      )
    )
  )
