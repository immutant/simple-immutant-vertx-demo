(ns demo.processing
  (:require [immutant.messaging :as msg]
            [immutant.jobs :as jobs]))

(defn- process
  "Processes the incoming message. A stand-in for a potentially more complex process."
  [m]
  (.toUpperCase m))

(defn- setup-ping
  "Sends a ping message to the given dest every 10s"
  [dest]
  (jobs/schedule :ping #(msg/publish dest {:ping (System/currentTimeMillis)})
                 :every [10 :seconds]))

(defn start [{:keys [request-dest response-dest] :as destinations}]
  (mapv msg/start (vals destinations))
  (msg/listen request-dest
              #(msg/publish response-dest (process %)))
  (setup-ping response-dest))
