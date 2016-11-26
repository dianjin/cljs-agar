(ns agar.constants)

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Server
; ~~~~~~~~~~~~~~~~~~~~~~~~

(def tick-interval 50)

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Game physics
; ~~~~~~~~~~~~~~~~~~~~~~~~

(def min-x -750)
(def min-y -750)
(def max-x 750)
(def max-y 750)
(def target-edibles 60)
(def cell-size 50)
(def radius-boost 0.5)
(def inverse-radius-speed-factor 3.88)

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Colors
; ~~~~~~~~~~~~~~~~~~~~~~~~

(def player-colors ["#64B5BA" "#FC537A" "#B0DFBA" "#F7B7AA"])
(def line-color "#64B5BA")
(def background-color "#E9EACA")

; ~~~~~~~~~~~~~~~~~~~~~~~~
; Player constants
; ~~~~~~~~~~~~~~~~~~~~~~~~

(defn type->radius
  [type]
  (case type
    :user 18
    :cpu 18
    :edible 12
    )
  )

(defn type->alive
  [type]
  (case type
    :user false
    :cpu true
    :edible true
    )
  )
