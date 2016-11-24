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
      #(-> % model/move-players model/set-overlapping-pairs)
      )
    )
  )
