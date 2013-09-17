(ns demo.core
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [hiccup.page :as page]
            [hiccup.form :as form]
            [immutant.web :as web]
            [immutant.daemons :as daemon]
            [vertx.embed :as vembed :refer [with-vertx]]
            [vertx.http :as http]
            [vertx.http.sockjs :as sockjs]
            [vertx.eventbus :as eb]))

(defn- home []
  (page/html4
   [:head
    (page/include-js "client.js")
    (page/include-css "client.css")]
   [:body
    [:h2 "Simple Vert.x EventBus Echo Demo"]
    (form/text-field :message)
    [:button {:id "send-message"} "Send Message"]
    [:div
     [:div {:id "sent"}
      [:h4 "Sent Messages"]]
     [:div {:id "rcvd"}
      [:h4 "Received Messages"]]]]))

(defroutes routes
  (GET "/" [] (home))
  (route/resources "/")
  (route/not-found "<h1>Not Found</h1>"))

(def app (-> routes handler/site))

(defn- start-web []
  (web/start app))

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

(defn- start-daemon []
    (let [state (atom {})]
    (daemon/daemonize "bridge"
                      #(daemon-up state)
                      #(daemon-down state))))
(defn init []
  (start-web)
  (start-daemon))
