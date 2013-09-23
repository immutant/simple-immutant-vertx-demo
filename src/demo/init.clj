(ns demo.init
  (:require [demo.web :as web]
            [demo.daemon :as daemon]
            [demo.processing :as processing]))

(def destinations {:response-dest "topic.response"
                   :request-dest "queue.request"})
(defn init []
  (web/start)
  (processing/start destinations)
  (daemon/start destinations))
