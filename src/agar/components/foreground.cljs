(ns agar.components.foreground
  (:require
    [agar.constants :as constants]
    )
  )

(defn connected-player
  [{cx :x cy :y} {:keys [radius color]}]
  [:circle {
    :cx cx
    :cy cy
    :r radius
    :fill color
    }]
  )

(defn body
  [{cx :x cy :y} {ox :x oy :y} radius color {x :x y :y}]
  (let [dx (- x ox) dy (- y oy)]
    [:circle {
      :cx (+ cx dx)
      :cy (+ cy dy)
      :r radius
      :fill color
      }]
    )
  )

(defn other-player
  [center origin [uid {:keys [radius color position]}]]
  (body center origin radius color position)
  )

(defn edible
  [center origin [_ {:keys [color position]}]]
  (body center origin 5 color position)
  )

(defn all-bodies
  [center {origin :position :as player} other-players edibles]
  (into [:g]
    (reverse
      (cons
        (connected-player center player)
        (concat
          (map (partial other-player center origin) other-players)
          (map (partial edible center origin) edibles)
          )
        )
      )
    )
  )
