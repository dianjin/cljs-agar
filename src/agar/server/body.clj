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
    :color (rand-nth constants/edible-colors)
    :radius constants/initial-edible-radius
    :position position
    :velocity {:x 0.0 :y 0.0}
  })

(defn initial-edibles
  []
  (apply
    merge
    (map-indexed
      (fn [idx pos] {(str "e-" idx) (position->edible pos)})
      (repeatedly
        constants/target-edibles
        #(physics/random-position)
        )
      )
    )
  )

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Players
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn default-player
  [uid] {
    :position {:x 0.0 :y 0.0}
    :velocity {:x 0.0 :y 0.0}
    :radius constants/initial-player-radius
    :color (get constants/player-colors (rem uid (count constants/player-colors)))
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

(defn remove-eatens
  [ids players]
  (reduce
    (fn [m id]
      (dissoc m id)
      )
    players
    ids
    )
  )

(defn augment-eaters
  [ids players]
  (reduce
    (fn [m id]
      (update-in m [id :radius] #(+ constants/radius-boost %))
      )
    players
    ids
    )
  )
