(ns agar.view
  (:require
    [agar.model :as model]
    [agar.communication :as communication]
    [agar.components.background :as background]
    [agar.components.foreground :as foreground]
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
      (foreground/connected-user width height)
      ]
    )
  )

(defn control-panel
  []
  (let [
    uid (:uid @model/state)
    user (get-in @model/state [:remote :users uid])
    ]
    [:div {
      :style {
        :position "absolute" :top "0" :left "0"
        :background-color "black"
        :padding "10px"
        :color "white"
        }
      }
      [:div (str "Position: " (:position user))]
      [:div (str "Velocity: " (:velocity user))]
      ]
    )
  )

(defn main
  []
  [:div
    (render-svg)
    (control-panel)
    ]
  )
