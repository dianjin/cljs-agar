(ns agar.constants)

; Server

(def tick-interval 50)
(def min-x -1000)
(def min-y -1000)
(def max-x 1000)
(def max-y 1000)
(def target-edibles 100)
(def radius-boost 0.5)
(def initial-edible-radius 12)
(def initial-player-radius 18)
(def base-speed 0.1)
(def speed-drop-per-radius 0.001)

; Client

(def cell-size 50)

; Colors

(def shared-colors ["#64B5BA" "#FC537A" "#B0DFBA" "#F7B7AA"])
(def edible-colors shared-colors)
(def player-colors shared-colors)
(def line-color "#64B5BA")
(def background-color "#E9EACA")
