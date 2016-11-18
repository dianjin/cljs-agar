(ns sente_reagent_starter.server.main
  (:require
    [org.httpkit.server :as server]
    [sente_reagent_starter.server.routes :as routes]
    [environ.core :as environ]
    )
  (:gen-class)
  )

(defn -main [& args]
  (println "Server starting...")
  (routes/start-websocket)
  (routes/start-router)
  (routes/start-ticker)
  (server/run-server #'routes/handler
                     {:port (or (some-> (first args) (Integer/parseInt))
                                (environ/env :http-port 3000))}))
