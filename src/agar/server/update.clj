(ns agar.server.update
  (:require
    [agar.server.model :as model]
    )
  )

(defn next-uid
  [_]
  (dosync
    (alter
      model/remote
      #(update % :player-counter inc)
      )
    )
  (@model/remote :player-counter)
  )

(defn add-player
  [uid]
  (dosync
    (alter
      model/remote
      (partial model/add-player uid)
      )
    )
  )

(defn remove-player
  [uid]
  (dosync
    (alter
      model/remote
      #(update-in % [:players] dissoc uid)
      )
    )
  )

(defn start-play
  [uid]
  (dosync
    (alter
      model/remote
      #(assoc-in % [:players uid :alive] true)
      )
    )
  )

(defn add-cpu
  []
  (dosync
    (alter
      model/remote
      #(model/add-cpu %)
      )
    )
  )

(defn set-mouse-position
  [uid position]
  (dosync
    (alter
      model/remote
      (partial model/set-mouse-position uid position)
      )
    )
  )

(defn tick
  []
  (dosync
    (alter
      model/remote
      #(-> %
        model/steer-players
        model/move-players
        model/eat-players
        )
      )
    )
  )
