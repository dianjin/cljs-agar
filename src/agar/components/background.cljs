(ns agar.components.background
  (:require
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
    x-offset 0
    y-offset 0
    coord-mapper #(* constants/cell-size %)
    x-repetitions (quot width constants/cell-size)
    x-list (map coord-mapper (range (inc x-repetitions)))
    y-repetitions (quot height constants/cell-size)
    y-list (map coord-mapper (range (inc y-repetitions)))
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
