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

; Event handlers

(defn mouse-move-handler
  [cx cy e]
  (let [x (.-clientX e) y (.-clientY e)]
    (communication/update-mouse-position
      {:x (- x cx) :y (- y cy)}
      )
    )
  )

; Views

(defn render-svg
  []
  (let [
    window (-> (dom/getWindow) dom/getViewportSize)
    width (.-width window)
    height (.-height window)
    cx (quot width 2)
    cy (quot height 2)
    ]
    [:svg {
      :on-mouse-move (partial mouse-move-handler cx cy)
      :width width
      :height height
      }
      (background/grid width height)
      (foreground/all-users cx cy)
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
