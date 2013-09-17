(ns demo.init
  (:require [demo.web :as web]
            [demo.daemon :as daemon]))

(defn init []
  (web/start)
  (daemon/start))
