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

(def player-colors ["#4EB3DE" "#8DE0A6" "#FEDD30" "#f8875f" "#ff79b2"])
(def num-colors (count player-colors))
(def line-color "#4EB3DE")
(def background-color "white")

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
