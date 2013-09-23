(ns demo.bridge
  (:require [vertx.embed :as vembed :refer [with-vertx]]
            [vertx.http :as http]
            [vertx.http.sockjs :as sockjs]
            [vertx.eventbus :as eb]
            [immutant.messaging :as msg]))

(defn dest->eventbus
  "Sets up a bridge to copy messages from an Immutant messaging dest to a Vertx address."
  ([vertx dest address]
     (msg/listen dest #(with-vertx vertx
                         (eb/publish address %)))))

(defn eventbus->dest
  "Sets up a bridge to copy messages from a Vertx address to an Immutant messaging dest."
  [vertx address dest]
  (with-vertx vertx
    (eb/on-message address (partial msg/publish dest))))

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
  "Initializes the embedded vertx instance, bridges to Immutant destinations, and starts the sockjs bridge."
  [{:keys [request-dest response-dest]}]
  (let [vertx (vembed/vertx)]
    (eventbus->dest vertx "demo.request" request-dest)
    (dest->eventbus vertx response-dest "demo.response")
    {:vertx vertx
     :server (start-sockjs-bridge vertx "localhost" 8081 "/eventbus")}))
