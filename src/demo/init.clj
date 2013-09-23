(ns demo.init
  (:require [demo.web :as web]
            [demo.daemon :as daemon]
            [immutant.messaging :as msg]))

(def config {:response-dest "topic.response"
             :request-dest "queue.request"
             :process-fn (memfn toUpperCase)})

(defn init []
  (let [{:keys [request-dest response-dest process-fn]} config]
    (msg/start request-dest)
    (msg/start response-dest)
    (msg/listen request-dest
                #(msg/publish response-dest (process-fn %))))
  (web/start)
  (daemon/start config))
