(ns agar.model
  (:require
    [reagent.core :as r]
    )
  )

(defonce state
  (r/atom {
    :local nil
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
