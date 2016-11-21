(ns agar.components.background
  (:require
    [agar.model :as model]
    [agar.constants :as constants]
    )
  )

(def line-attrs {
  :stroke "#ccc"
  :stroke-width 1
  })

(defn grid
  [width height]
  (let [
    uid (:uid @model/state)
    position (get-in @model/state [:remote :users uid :position])
    origin-x (:x position)
    origin-y (:y position)
    x-offset (- 0 (rem origin-x constants/cell-size))
    y-offset (- 0 (rem origin-y constants/cell-size))
    coord-mapper #(* constants/cell-size %)
    x-repetitions (+ 2 (quot width constants/cell-size))
    x-list (map coord-mapper (range x-repetitions))
    y-repetitions (+ 2 (quot height constants/cell-size))
    y-list (map coord-mapper (range y-repetitions))
    ]
    [:g
      (for [x x-list]
        ^{:key x}
        [:line
          (merge
            line-attrs {
              :x1 (+ x x-offset) :y1 0
              :x2 (+ x x-offset) :y2 height
              }
            )
          ]
        )
      (for [y y-list]
        ^{:key y}
        [:line
          (merge
            line-attrs {
              :y1 (+ y y-offset) :x1 0
              :y2 (+ y y-offset) :x2 width
              }
            )
          ]
        )
      ]
    )
  )
