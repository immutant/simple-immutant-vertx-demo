(ns demo.processing
  (:require [immutant.messaging :as msg]))

(defn- process
  "Processes the incoming message. A stand-in for a potentially more complex process."
  [m]
  (.toUpperCase m))

(defn start [{:keys [request-dest response-dest] :as destinations}]
  (mapv msg/start (vals destinations))
  (msg/listen request-dest
              #(msg/publish response-dest (process %))))
