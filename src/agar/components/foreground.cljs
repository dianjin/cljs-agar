(ns agar.components.foreground
  (:require
    [agar.constants :as constants]
    [agar.model :as model]
    )
  )

(defn connected-user
  [{cx :x cy :y} {:keys [radius color]}]
  [:circle {
    :cx cx
    :cy cy
    :r radius
    :fill color
    }]
  )

(defn alien
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

(defn other-user
  [center origin [uid {:keys [radius color position]}]]
  (alien center origin radius color position)
  )

(defn edible
  [center origin {:keys [color position]}]
  (alien center origin 5 color position)
  )

(defn all-users
  [center]
  (let [
    uid (:uid @model/state)
    all-users (get-in @model/state [:remote :users])
    edibles (get-in @model/state [:remote :edibles])
    user (get all-users uid)
    other-users (seq (dissoc all-users uid))
    origin (:position user)
    ]
    (into [:g]
      (reverse
        (cons
          (connected-user center user)
          (concat
            (map (partial other-user center origin) other-users)
            (map (partial edible center origin) edibles)
            )
          )
        )
      )
    )
  )
