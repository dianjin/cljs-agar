(ns ^:figwheel-always cljsagar.main
  (:require
    [cljsagar.ainit]
    [cljsagar.view :as view]
    [reagent.core :as r]
    )
  )

(r/render-component
  [view/main]
  (.getElementById js/document "app")
  )
