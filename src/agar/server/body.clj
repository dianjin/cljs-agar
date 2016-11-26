(ns agar.server.body
  (:require
    [agar.physics :as physics]
    [agar.constants :as constants]
    )
  )

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Bodies
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn steer-player-towards
  [mouse-from-origin {:keys [radius] :as player}]
  (let [
    {norm-x :x norm-y :y} (physics/norm mouse-from-origin)
    distance (physics/magnitude mouse-from-origin)
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

(defn move-player
  [m uid {:keys [position velocity radius] :as player}]
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
    (assoc m uid (assoc player :position position-prime))
    )
  )

(defn edible?
  [{:keys [alive]}]
  alive
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
; Edibles
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn position->edible
  [position] {
    :type :edible
    :alive true
    :color (rand-nth constants/edible-colors)
    :position position
    :velocity {:x 0.0 :y 0.0}
    :radius constants/initial-edible-radius
  })

(defn initial-edibles
  []
  (apply
    merge
    (map-indexed
      (fn [idx pos] {(str "e-" idx) (position->edible pos)})
      (repeatedly
        constants/target-edibles
        #(physics/random-position constants/initial-edible-radius)
        )
      )
    )
  )

; ~~~~~~~~~~~~~~~~~~~~~~~~
; CPU
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn default-cpu
  [] {
    :type :cpu
    :alive true
    :color (rand-nth constants/edible-colors)
    :position (physics/random-position constants/initial-player-radius)
    :velocity {:x 0.0 :y 0.0}
    :radius constants/initial-player-radius
  })

(defn most-edible
  [players {my-radius :radius my-position :position my-velocity :velocity :as player}]
  (reduce-kv
    (fn
      [current-most-edible _ {:keys [radius position velocity] :as current-player}]
      (if (nil? current-most-edible)
        (assoc current-player :distance (physics/distance my-position position))
        (if (and (> my-radius radius) (physics/faster? my-velocity velocity))
          (if (< (physics/distance my-position position) (:distance current-most-edible))
            (assoc current-player :distance (physics/distance my-position position))
            current-most-edible
            )
          current-most-edible
          )
        )
      )
    nil
    players
    )
  )

(defn steer-cpu
  [players player]
  (let [
    most-edible-player (most-edible players player)
    target-vector (physics/vector-from-to (:position player) (:position most-edible-player))
    ]
    (steer-player-towards target-vector player)
    )
  )

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Players
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn default-player
  [uid] {
    :type :player
    :alive false
    :color (rand-nth constants/edible-colors)
    :position (physics/random-position constants/initial-player-radius)
    :velocity {:x 0.0 :y 0.0}
    :radius constants/initial-player-radius
  })

(defn steer-player
  [players {:keys [type] :as player}]
  (case type
    :edible player
    :player player
    :cpu (steer-cpu players player)
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

(defn update-eatens
  [ids players]
  (reduce
    (fn [m id]
      (if (= :edible (get-in m [id :type]))
        (update m id reset-player-position)
        (update m id kill-player)
        )
      )
    players
    ids
    )
  )

(defn update-eaters
  [ids players]
  (reduce
    (fn [m id]
      (update-in m [id :radius] #(+ constants/radius-boost %))
      )
    players
    ids
    )
  )
