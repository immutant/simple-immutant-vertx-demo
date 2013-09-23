(ns demo.daemon
  (:require [immutant.daemons :as daemon]
            [demo.bridge :as bridge]))

(defn- daemon-up
  [state opts]
  (reset! state (bridge/init-bridge opts)))

(defn- daemon-down [state]
  (.close (:server @state))
  (.stop (:vertx @state)))

(defn start [opts]
    (let [state (atom {})]
    (daemon/daemonize "bridge"
                      #(daemon-up state opts)
                      #(daemon-down state))))
