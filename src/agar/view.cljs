(ns agar.view
  (:require
    [agar.model :as model]
    [agar.communication :as communication]
    [agar.constants :as constants]
    [agar.components.background :as background]
    [agar.components.foreground :as foreground]
    [agar.physics :as physics]
    [clojure.string :as string]
    [goog.events :as events]
    [goog.events.KeyCodes :as KeyCodes]
    [goog.crypt :as crypt]
    [goog.dom :as dom]
    )
  )

; Event handlers

(defn mouse-move-handler
  [{cx :x cy :y} e]
  (let [x (.-clientX e) y (.-clientY e)]
    (communication/set-mouse-position
      {:x (- x cx) :y (- y cy)}
      )
    )
  )

; Views

(defn render-svg
  [uid {:keys [players]}]
  (let [
    other-players (seq (dissoc players uid))
    player (get players uid)
    window (-> (dom/getWindow) dom/getViewportSize)
    width (.-width window)
    height (.-height window)
    center {:x (quot width 2) :y (quot height 2)}
    ]
    [:svg {
      :on-mouse-move (partial mouse-move-handler center)
      :width width
      :height height
      }
      (background/grid width height player)
      (background/borders center player)
      (foreground/all-bodies center player other-players)
      ]
    )
  )

(defn control-panel
  [uid {:keys [players]}]
  (let [{:keys [position velocity]} (get players uid)]
    [:div {
      :style {
        :position "absolute" :top "0" :left "0"
        :background-color "black"
        :padding "10px"
        :color "white"
        }
      }
      [:div (str "Position: " position)]
      [:div (str "Velocity: " velocity)]
      [:div (str "Speed: " (physics/magnitude velocity))]
      ]
    )
  )

(defn main
  []
  (let [{:keys [remote uid]} @model/state]
    [:div {
      :style {
        :background-color constants/background-color
        }
      }
      (render-svg uid remote)
      (control-panel uid remote)
      ]
    )
  )
