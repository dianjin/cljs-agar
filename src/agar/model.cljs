(ns agar.model
  (:require
    [reagent.core :as reagent]
    )
  )

(defonce state
  (reagent/atom {
    :remote nil
    :uid nil
    })
  )

(defn remote! [world]
  (swap! state assoc :remote world)
  )

(defn uid! [uid]
  (swap! state assoc :uid uid)
  )
