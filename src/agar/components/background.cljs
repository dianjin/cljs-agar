(ns agar.components.background
  (:require
    [agar.constants :as constants]
    )
  )

(def border-attrs {
  :stroke constants/line-color
  :stroke-width 3
  })

(defn borders
  [{cx :x cy :y} {{ox :x oy :y} :position}]
  (let [
    x1 (- cx (- ox constants/min-x))
    x2 (- cx (- ox constants/max-x))
    y1 (- cy (- oy constants/min-y))
    y2 (- cy (- oy constants/max-y))
    ]
    [:g
      [:line (merge border-attrs {:x1 x1 :x2 x2 :y1 y1 :y2 y1})]
      [:line (merge border-attrs {:x1 x1 :x2 x2 :y1 y2 :y2 y2})]
      [:line (merge border-attrs {:x1 x1 :x2 x1 :y1 y1 :y2 y2})]
      [:line (merge border-attrs {:x1 x2 :x2 x2 :y1 y1 :y2 y2})]
      ]
    )
  )

(def line-attrs {
  :stroke constants/line-color
  :stroke-width 1
  })

(defn grid
  [width height {{ox :x oy :y} :position}]
  (let [
    x-offset (- 0 (rem ox constants/cell-size))
    y-offset (- 0 (rem oy constants/cell-size))
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
