(ns agar.model
  (:require
    [reagent.core :as r]
    )
  )

(defonce my-state
  (r/atom {
    :local nil
    :remote nil
    :uid nil
    })
  )

(defn remote! [world]
  (swap! my-state assoc :remote world)
  )

(defn uid! [uid]
  (swap! my-state assoc :uid uid)
  )
