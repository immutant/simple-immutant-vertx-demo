(ns demo.daemon
  (:require [immutant.daemons :as daemon]
            [demo.bridge :as bridge]))

(defn- daemon-up
  [state config]
  (reset! state (bridge/init-bridge config)))

(defn- daemon-down [state]
  (.close (:server @state))
  (.stop (:vertx @state)))

(defn start [config]
    (let [state (atom {})]
    (daemon/daemonize "bridge"
                      #(daemon-up state config)
                      #(daemon-down state))))
