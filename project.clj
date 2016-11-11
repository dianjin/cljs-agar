(defproject sente_reagent_starter "0.1.0-SNAPSHOT"
  :description "TODO"
  :url "TODO"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.5.3"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.34"]
                 [org.clojure/core.async "0.2.374"]
                 [org.clojure/core.memoize "0.5.8"]
                 [reagent "0.5.1"]
                 [com.taoensso/sente "1.8.1"]
                 [environ "1.0.2"]
                 [http-kit "2.1.19"]
                 [compojure "1.5.0"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.2.0"]
                 [ring-cors "0.1.7"]]

  :plugins [[lein-figwheel "0.5.0-6"]
            [lein-cljsbuild "1.1.3"]
            [lein-environ "1.0.2"]]

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :main sente_reagent_starter.server.main

  :uberjar-name "sente_reagent_starter-standalone.jar"

  :profiles
  {:dev {:env {:dev? "true"}
         :cljsbuild {:builds
                     [{:id "dev"
                       :source-paths ["src" "dev"]
                       :figwheel {}
                       :compiler {:main sente_reagent_starter.main
                                  :asset-path "js/compiled/out"
                                  :output-to "resources/public/js/compiled/sente_reagent_starter.js"
                                  :output-dir "resources/public/js/compiled/out"
                                  :source-map-timestamp true}}]}}
   :uberjar {:hooks [leiningen.cljsbuild]
             :aot :all
             :cljsbuild {:builds
                         [{:id "min"
                           :source-paths ["src" "prod"]
                           :compiler {:main sente_reagent_starter.main
                                      :output-to "resources/public/js/compiled/sente_reagent_starter.js"
                                      :optimizations :advanced
                                      :pretty-print false}}]}}}



  :figwheel {:css-dirs ["resources/public/css"] })
