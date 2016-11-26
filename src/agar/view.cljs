(ns agar.view
  (:require
    [agar.communication :as communication]
    [agar.components.background :as background]
    [agar.components.foreground :as foreground]
    [agar.components.information :as information]
    [agar.constants :as constants]
    [agar.model :as model]
    [agar.physics :as physics]
    [goog.dom :as dom]
    )
  )

; Event handlers

(defn mouse-move-handler
  [{cx :x cy :y} e]
  (let [x (.-clientX e) y (.-clientY e)]
    (communication/steer-user
      {:x (- x cx) :y (- y cy)}
      )
    )
  )

; Views

(defn render-information
  [uid players]
  (information/status uid players)
  )

(defn render-svg
  [uid {:keys [players]} center width height]
  (let [
    other-players (seq (dissoc players uid))
    {:keys [alive] :as player} (get players uid)
    mouse-handler (if alive (partial mouse-move-handler center) #())
    ]
    [:svg {
      :on-mouse-move mouse-handler
      :width width
      :height height
      }
      (background/grid width height player)
      (background/borders center player)
      (foreground/all-bodies center player other-players)
      ]
    )
  )

(defn main
  []
  (let [
    {:keys [remote uid]} @model/state
    window (-> (dom/getWindow) dom/getViewportSize)
    width (.-width window)
    height (.-height window)
    center {:x (quot width 2) :y (quot height 2)}
    ]
    [:div {
      :style {
        :background-color constants/background-color
        }
      }
      (render-svg uid remote center width height)
      (render-information uid (:players remote))
      ]
    )
  )
