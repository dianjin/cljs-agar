(ns agar.components.foreground
  (:require
    [agar.constants :as constants]
    [agar.model :as model]
    )
  )

(def circle-defaults {
  :fill "rgba(255,0,0,0.1)"
  :stroke "black"
  :stroke-width 1
  })

(defn connected-user
  [width height]
  (let [
    uid (:uid @model/state)
    user (get-in @model/state [:remote :users uid])
    radius (:radius user)
    center-x (quot width 2)
    center-y (quot height 2)
    ]
    [:circle
      (merge
        circle-defaults
        {:cx center-x :cy center-y :r radius}
        )
      ]
    )
  )
