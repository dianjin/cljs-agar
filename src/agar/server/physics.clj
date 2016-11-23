(ns agar.server.physics
  (:require
    [agar.constants :as constants]
    )
  )

(defn rand-int-between
  [a b]
  (let [diff (- b a)]
    (+ a (rand-int diff))
    )
  )

(defn radius-ratio->scaler
  [radius ratio]
  (/ (* radius ratio) 1000)
  )

(defn distance
  [{x1 :x y1 :y} {x2 :x y2 :y}]
  (let [dx (- x2 x1) dy (- y2 y1)]
    (Math/sqrt (+ (Math/pow dx 2) (Math/pow dy 2)))
    )
  )

(defn random-position
  []
  {
    :x (rand-int-between constants/min-x constants/max-x)
    :y (rand-int-between constants/min-y constants/max-y)
  }
  )

(defn x-out-of-bounds?
  [x]
  (or
    (< x constants/min-x)
    (> x constants/max-x)
    )
  )

(defn y-out-of-bounds?
  [y]
  (or
    (< y constants/min-y)
    (> y constants/max-y)
    )
  )
