(ns ^:figwheel-always agar.main
  (:require
    [agar.ainit]
    [agar.view :as view]
    [reagent.core :as r]
    )
  )

(r/render-component
  [view/main]
  (.getElementById js/document "app")
  )
