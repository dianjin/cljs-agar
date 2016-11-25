(ns agar.physics
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

(defn radius->max-speed
  [radius]
  (let [radius-diff (- radius constants/initial-player-radius)]
    (-
      constants/base-speed
      (* constants/speed-drop-per-radius radius-diff)
      )
    )
  )

(defn distance
  [{x1 :x y1 :y} {x2 :x y2 :y}]
  (let [dx (- x2 x1) dy (- y2 y1)]
    (Math/sqrt (+ (Math/pow dx 2) (Math/pow dy 2)))
    )
  )

(defn magnitude
  [vector]
  (distance {:x 0 :y 0} vector)
  )

(defn norm
  [{:keys [x y] :as vector}]
  (let [size (magnitude vector)]
    (if (zero? size)
      {:x 0 :y 0}
      {:x (/ x size) :y (/ y size)}
      )
    )
  )

(defn random-position
  [radius]
  (let [
    min-x (+ constants/min-x radius)
    max-x (- constants/max-x radius)
    min-y (+ constants/min-y radius)
    max-y (- constants/max-y radius)
    ] {
      :x (rand-int-between min-x max-x)
      :y (rand-int-between min-y max-y)
    })
  )

(defn x-out-of-bounds?
  [radius x]
  (or
    (< (- x radius) constants/min-x)
    (> (+ x radius) constants/max-x)
    )
  )

(defn y-out-of-bounds?
  [radius y]
  (or
    (< (- y radius) constants/min-y)
    (> (+ y radius) constants/max-y)
    )
  )

(defn overlapping?
	[[_ {p1 :position}] [_ {p2 :position r-big :radius}]]
	(< (distance p1 p2) r-big)
	)

(defn overlapping-pairs
	[list-bodies current-overlapping-pairs]
	(if (<= (count list-bodies) 1)
		(map
      (fn [[[id-1 _] [id-2 _]]] [id-1 id-2])
      current-overlapping-pairs
      )
		(let [[head & rest] list-bodies]
			(recur
				rest
				(concat
					(filter
						(partial apply overlapping?)
						(map
              (fn [b] (sort-by #(:radius (second %)) [head b]))
              rest
              )
						)
					current-overlapping-pairs
					)
				)
			)
		)
	)
