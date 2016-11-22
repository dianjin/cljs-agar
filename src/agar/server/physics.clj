(ns agar.server.physics)

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
