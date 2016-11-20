(ns agar.view
  (:require
    [clojure.string :as string]
    [goog.events :as events]
    [goog.events.KeyCodes :as KeyCodes]
    [agar.model :as model]
    [agar.communication :as communication]
    [goog.crypt :as crypt]
    [goog.dom.forms :as forms]))

(defn render-users [users]
  [:div
    (str "Users connected: " (count users))
    [:ul
      (for [[uid {:keys [name]}] users]
        ^{:key uid} [:li name]
        )
      ]
    ]
  )

(defn main []
  (let [users (get-in @model/my-state [:remote :users])]
    [:div
      (some-> users render-users)
      ]
    )
  )
