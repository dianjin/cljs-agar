(ns agar.server.player
  (:require
    [agar.physics :as physics]
    [agar.constants :as constants]
    )
  )

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Initial values
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn type->player
  [type]
  (let [
    radius (constants/type->radius type)
    alive (constants/type->alive type)
    ] {
      :type type
      :alive alive
      :color (rand-nth constants/player-colors)
      :position (physics/random-position radius)
      :velocity {:x 0.0 :y 0.0}
      :radius radius
    })
  )

(defn initial-edibles
  []
  (apply
    merge
    (map-indexed
      (fn [idx pos] {(str "e-" idx) (type->player :edible)})
      (range constants/target-edibles)
      )
    )
  )

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Helper functions
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn edible?
  [{:keys [alive]}]
  alive
  )

(defn most-edible
  [players {r :radius p :position :as player}]
  (reduce-kv
    (fn [c-most-edible _ {:keys [radius position] :as c-player}]
      (let [c-dist (physics/distance p position)]
        (if (nil? c-most-edible)
          (assoc c-player :distance c-dist)
          (if
            (and
              (> r radius)
              (edible? c-player)
              (< c-dist (:distance c-most-edible))
              )
            (assoc c-player :distance c-dist)
            c-most-edible
            )
          )
        )
      )
    nil
    players
    )
  )

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Updaters
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn steer-player-towards
  [target-vector {:keys [radius] :as player}]
  (let [
    {norm-x :x norm-y :y} (physics/normalize target-vector)
    distance (physics/magnitude target-vector)
    ratio (if (< distance radius) (/ distance radius) 1)
    max-speed (physics/radius->max-speed radius)
    ]
    (assoc
      player
      :velocity
      {:x (* max-speed ratio norm-x) :y (* max-speed ratio norm-y)}
      )
    )
  )

(defn steer-cpu
  [players {:keys [position] :as player}]
  (-> (:position (most-edible players player))
    (#(physics/vector-from-to position %))
    (#(steer-player-towards % player))
    )
  )

(defn move-player
  [{:keys [position velocity radius] :as player}]
  (let [
    {x :x y :y} position
    {vx :x vy :y} velocity
    delta constants/tick-interval
    dx (* delta vx) dy (* delta vy)
    x-prime (+ x dx) y-prime (+ y dy)
    x-prime-2 (if (physics/x-out-of-bounds? radius x-prime) x x-prime)
    y-prime-2 (if (physics/y-out-of-bounds? radius y-prime) y y-prime)
    position-prime {:x x-prime-2 :y y-prime-2}
    ]
    (assoc player :position position-prime)
    )
  )

(defn kill-player
  [player]
  (assoc
    player
    :alive false
    :velocity {:x 0 :y 0}
    )
  )

(defn reset-player-position
  [{:keys [radius] :as player}]
  (assoc
    player
    :position
    (physics/random-position radius)
    )
  )

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Mass updaters
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn move-players
  [players]
  (reduce-kv
    (fn [p-map id player]
      (assoc p-map id (move-player player))
      )
    {}
    players
    )
  )

(defn steer-cpus
  [players]
  (reduce-kv
    (fn [p-map id {:keys [type alive] :as player}]
      (case type
        :cpu (assoc p-map id (steer-cpu players player))
        (assoc p-map id player)
        )
      )
    {}
    players
    )
  )

(defn update-eatens
  [ids players]
  (reduce
    (fn [p-map id]
      (case (get-in p-map [id :type])
        :edible (update p-map id reset-player-position)
        (update p-map id kill-player)
        )
      )
    players
    ids
    )
  )

(defn update-eaters
  [ids players]
  (reduce
    (fn [p-map id]
      (update-in p-map [id :radius] #(+ constants/radius-boost %))
      )
    players
    ids
    )
  )