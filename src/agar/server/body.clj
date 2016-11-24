(ns agar.server.body
  (:require
    [agar.server.physics :as physics]
    [agar.constants :as constants]
    )
  )

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Edibles
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn position->edible
  [position] {
    :color (rand-nth constants/edible-colors)
    :radius 5
    :position position
  })

(defn initial-edibles
  []
  (reduce
    (fn [m [idx pos]]
      (assoc m idx (position->edible pos))
      )
    {}
    (map-indexed
      (fn [idx pos]
        [idx pos]
        )
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
    :name (str "Anonymous " uid)
    :position {:x 0.0 :y 0.0}
    :velocity {:x 0.0 :y 0.0}
    :radius 10
    :color (get constants/player-colors (rem uid (count constants/player-colors)))
  })

(defn move-player
  [m uid {:keys [position velocity] :as player}]
  (let [
    {x :x y :y} position
    {vx :x vy :y} velocity
    delta constants/tick-interval
    dx (* delta vx) dy (* delta vy)
    x-prime (+ x dx) y-prime (+ y dy)
    x-prime-2 (if (physics/x-out-of-bounds? x-prime) x x-prime)
    y-prime-2 (if (physics/y-out-of-bounds? y-prime) y y-prime)
    position-prime {:x x-prime-2 :y y-prime-2}
    ]
    (assoc m uid (assoc player :position position-prime))
    )
  )

(defn steer-player
  [{:keys [x y] :as mouse-from-origin} player]
  (let [
    {radius :radius} player
    real-distance (physics/distance {:x 0 :y 0} mouse-from-origin)
    ratio (if (< real-distance radius) 1 (/ radius real-distance))
    scaler (physics/radius-ratio->scaler radius ratio)
    ]
    (assoc
      player
      :velocity
      {:x (* scaler x) :y (* scaler y)}
      )
    )
  )
