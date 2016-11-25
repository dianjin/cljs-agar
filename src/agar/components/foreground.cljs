(ns agar.components.foreground
  (:require
    [agar.constants :as constants]
    )
  )

(defn body
  [{cx :x cy :y} {ox :x oy :y} {:keys [position alive color radius]}]
  (let [dx (- (:x position) ox) dy (- (:y position) oy)]
    [:circle {
      :cx (+ cx dx)
      :cy (+ cy dy)
      :r radius
      :fill color
      :fill-opacity (if alive 1 0.5)
      }]
    )
  )

(defn all-bodies
  [center {origin :position :as player} other-players]
  (into [:g]
    (reverse
      (cons
        (body center origin player)
        (map
          (fn [[_ other-player]] (body center origin other-player))
          other-players
          )
        )
      )
    )
  )
