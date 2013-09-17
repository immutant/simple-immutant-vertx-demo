(ns demo.daemon
  (:require [immutant.daemons :as daemon]
            [demo.bridge :as bridge]))

(defn- daemon-up
  [state]
  (reset! state (bridge/init-bridge)))

(defn- daemon-down [state]
  (.close (:server @state))
  (.stop (:vertx @state)))

(defn start []
    (let [state (atom {})]
    (daemon/daemonize "bridge"
                      #(daemon-up state)
                      #(daemon-down state))))
