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

(defn render-status
  [uid remote center]
  (let [alive (get-in remote [:players uid :alive])]
    (if (not alive)
      [:input {
        :on-click #(communication/start-play)
        :type "button"
        :value "Play!"
        :style {
          :width 50
          :position "absolute"
          :top (+ (:y center) 30)
          :left (- (:x center) 25)
          :padding 10
          }
        }
        ]
        [:span]
      )
    )
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
      (render-status uid remote center)
      ]
    )
  )
