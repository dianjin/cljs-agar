(ns agar.view
  (:require
    [agar.model :as model]
    [agar.communication :as communication]
    [agar.components.background :as background]
    [clojure.string :as string]
    [goog.events :as events]
    [goog.events.KeyCodes :as KeyCodes]
    [goog.crypt :as crypt]
    [goog.dom :as dom]
    )
  )

(defn render-svg
  []
  (let [
    window (-> (dom/getWindow) dom/getViewportSize)
    width (.-width window)
    height (.-height window)
    ]
    [:svg {:width width :height height}
      (background/grid width height)
      ]
    )
  )
;
(defn main
  []
  (let [
    users (get-in @model/my-state [:remote :users])
    ]
    [:div
      (render-svg)
      ]
    )
  )
