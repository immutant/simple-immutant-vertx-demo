(ns demo.bridge
  (:require [vertx.embed :as vembed :refer [with-vertx]]
            [vertx.http :as http]
            [vertx.http.sockjs :as sockjs]
            [vertx.eventbus :as eb]))

(defn- start-sockjs-bridge
  "Creates a Vert.x http server, a sockjs server within that http
  server, then installs an eventbus bridge in the sockjs server."
  [vertx host port path]
  (println (format "Starting SockJS bridge at http://%s:%s%s" host port path))
  (with-vertx vertx
    (let [server (http/server)]
      (-> server
          (sockjs/sockjs-server)
          (sockjs/bridge {:prefix path} [{}] [{}]))
      (http/listen server port host))))

(defn init-bridge
  "Initializes the embedded vertx instance, sets up our echo handler,
   and starts the sockjs bridge."
  []
  (let [vertx (vembed/vertx)]
    (with-vertx vertx
      (eb/on-message "demo.request"
                     (partial eb/publish "demo.response")))
    {:vertx vertx
     :server (start-sockjs-bridge vertx "localhost" 8081 "/eventbus")}))
