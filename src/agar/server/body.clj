(ns agar.server.body
  (:require
    [agar.physics :as physics]
    [agar.constants :as constants]
    )
  )

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Edibles
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn position->edible
  [position] {
    :playable false
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
; Players
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn default-player
  [uid] {
    :playable true
    :alive false
    :color (get constants/player-colors (rem uid (count constants/player-colors)))
    :position (physics/random-position constants/initial-player-radius)
    :velocity {:x 0.0 :y 0.0}
    :radius constants/initial-player-radius
  })

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

(defn steer-player
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

(defn update-eatens
  [ids players]
  (reduce
    (fn [m id]
      (if (true? (get-in m [id :playable]))
        (update m id kill-player)
        (update m id reset-player-position)
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
