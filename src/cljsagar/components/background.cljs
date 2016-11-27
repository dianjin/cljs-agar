(ns cljsagar.components.background
  (:require
    [cljsagar.constants :as constants]
    )
  )

(def rect-attrs {
  :fill constants/line-color
  :fill-opacity 0.5
  })

(defn borders
  [{cx :x cy :y} {{ox :x oy :y} :position}]
  (let [
    x1 (max 0 (- cx (- ox constants/min-x)))
    x2 (- cx (- ox constants/max-x))
    y1 (max 0 (- cy (- oy constants/min-y)))
    y2 (- cy (- oy constants/max-y))
    width (- constants/max-x constants/min-x)
    height (- constants/max-y constants/min-y)
    ]
    [:g
      [:rect (merge rect-attrs {:x 0 :y 0 :width width :height y1})]
      [:rect (merge rect-attrs {:x 0 :y y1 :width x1 :height height})]
      [:rect (merge rect-attrs {:x x2 :y y1 :width width :height y2})]
      [:rect (merge rect-attrs {:x x1 :y y2 :width width :height y2})]
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
    mid-x (quot width 2) mid-y (quot height 2)
    x-offset (- 0 (rem (- ox mid-x) constants/cell-size))
    y-offset (- 0 (rem (- oy mid-y) constants/cell-size))
    coord-mapper (fn [idx coord] [idx (* constants/cell-size coord)])
    x-repetitions (+ 2 (quot width constants/cell-size))
    x-list (map-indexed coord-mapper (range x-repetitions))
    y-repetitions (+ 2 (quot height constants/cell-size))
    y-list (map-indexed coord-mapper (range y-repetitions))
    ]
    [:g
      (for [[idx x] x-list]
        ^{:key (str "x-" idx)}
        [:line
          (merge
            line-attrs {
              :x1 (+ x x-offset) :y1 0
              :x2 (+ x x-offset) :y2 height
              }
            )
          ]
        )
      (for [[idx y] y-list]
        ^{:key (str "y-" idx)}
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
