(ns cljsagar.components.information
  (:require
    [cljsagar.constants :as constants]
    [cljsagar.communication :as communication]
    )
  )

(def button-style {
  :background-color constants/line-color
  :color constants/background-color
  :border 0
  :margin-left 10
  :font-size "16px"
  :padding 10
  :float "left"
  })

(defn controls
  [{:keys [alive]}]
  [:div
    [:input {
      :type "button"
      :style
        (merge
          button-style
          (if alive {:color constants/line-color} {})
          )
      :disabled alive
      :on-click #(communication/start-play)
      :value "Play"
      }]
    [:input {
      :type "button"
      :style button-style
      :on-click #(communication/add-cpu)
      :value "Add CPU player"
      }]
    ]
  )

(defn status
  [uid players]
  (let [
    cpus (filter (fn [[_ {:keys [type]}]] (= type :cpu)) players)
    users (filter (fn [[_ {:keys [type]}]] (= type :user)) players)
    alive-users (filter (fn [[_ {:keys [alive]}]] alive) users)
    ]
    [:div {
      :style {
        :position "absolute"
        :bottom 10
        :left 0
        :width "100%"
        }
      }
      (controls (get players uid))
      [:span {:style button-style}
        (str
          (count users) " connected users "
          "(" (count alive-users) " alive)"
          )
        ]
      [:span {:style button-style}
        (str
          (count cpus) " CPU players"
          )
        ]
      ]
    )
  )
