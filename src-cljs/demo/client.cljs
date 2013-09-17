(ns demo.client
  (:require [enfocus.core :as ef]
            [enfocus.events :as events]
            [vertx.client.eventbus :as eb])
  (:require-macros [enfocus.macros :as em]))

(def eb (atom nil))

(defn open-eventbus
  "Opens a connection to the remote EventBus endpoint."
  [on-open]
  (reset! eb (eb/eventbus "http://localhost:8081/eventbus"))
  (eb/on-open @eb #(.log js/console "eventbus opened"))
  (eb/on-open @eb on-open))

(defn append-message [id m]
  (ef/at id (ef/append (ef/html [:div m]))))

(defn send-message [m]
  (eb/publish @eb "demo.request" m))

(defn attach-listeners []
  (eb/on-message @eb "demo.request" (partial append-message "#sent"))
  (eb/on-message @eb "demo.response" (partial append-message "#rcvd")))

(defn init []
  (open-eventbus attach-listeners)
  (ef/at "#send-message"
         (events/listen :click
                        #(send-message (ef/from "#message" (ef/get-prop :value))))))

(set! (.-onload js/window) init)


