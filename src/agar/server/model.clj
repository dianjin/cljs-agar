(ns agar.server.model
  (:require
    [agar.server.body :as body]
    [agar.server.physics :as physics]
    )
  )

; Remote state

(defonce remote
  (ref {
    :player-counter 0
    :players {}
    :edibles (body/initial-edibles)
    :overlapping-pairs []
    })
  )

; Remote updaters

(defn set-overlapping-pairs
  [{:keys [edibles players] :as remote}]
  (let [players-seq (seq players) edibles-seq (seq edibles)]
    (assoc
      remote
      :overlapping-pairs
      (physics/overlapping-pairs
        (concat players-seq edibles-seq)
        []
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
