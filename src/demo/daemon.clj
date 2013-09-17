(ns demo.daemon
  (:require [immutant.daemons :as daemon]
            [vertx.embed :as vembed :refer [with-vertx]]
            [vertx.http :as http]
            [vertx.http.sockjs :as sockjs]
            [vertx.eventbus :as eb]))

(defn- start-sockjs-bridge [vertx host port path]
  (println (format "Starting SockJS bridge at http://%s:%s%s" host port path))
  (with-vertx vertx
    (let [server (http/server)]
      (-> server
          (sockjs/sockjs-server)
          (sockjs/bridge {:prefix path} [{}] [{}]))
      (http/listen server port host))))

(defn- daemon-up
  [state]
  (let [vertx (vembed/vertx)]
    (swap! state assoc
           :vertx vertx
           :server (start-sockjs-bridge vertx "localhost" 8081 "/eventbus"))
    (with-vertx vertx
      (eb/on-message "demo.request"
                     (partial eb/publish "demo.response")))))

(defn- daemon-down [state]
  (.close (:server @state))
  (.stop (:vertx @state)))

(defn start []
    (let [state (atom {})]
    (daemon/daemonize "bridge"
                      #(daemon-up state)
                      #(daemon-down state))))
