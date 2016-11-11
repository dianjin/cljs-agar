(ns ^:figwheel-always sente_reagent_starter.main
  (:require
    [sente_reagent_starter.ainit]
    [sente_reagent_starter.view :as view]
    [reagent.core :as r]
    )
  )

(r/render-component
  [view/main]
  (. js/document (getElementById "app"))
  )
