(ns agar.components.foreground
  (:require
    [agar.constants :as constants]
    [agar.model :as model]
    )
  )

(defn connected-user
  [cx cy {:keys [radius color]}]
  [:circle {
    :cx cx
    :cy cy
    :r radius
    :fill color
    }]
  )

(defn other-user
  [cx cy user [uid {:keys [radius color position]}]]
  (let [
    {origin-x :x origin-y :y} (:position user)
    {x :x y :y} position
    dx (- x origin-x)
    dy (- y origin-y)
    ]
    [:circle {
      :cx (+ cx dx)
      :cy (+ cy dy)
      :r radius
      :fill color
      }]
    )
  )

(defn all-users
  [cx cy]
  (let [
    uid (:uid @model/state)
    all-users (get-in @model/state [:remote :users])
    user (get all-users uid)
    other-users (seq (dissoc all-users uid))
    {radius :radius color :color} user
    ]
    (into [:g]
      (reverse
        (cons
          (connected-user cx cy user)
          (map (partial other-user cx cy user) other-users)
          )
        )
      )
    )
  )
