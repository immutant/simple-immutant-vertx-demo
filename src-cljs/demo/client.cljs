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

(defn append-content
  "Append the given content to the element specified by id"
  [id content]
  (ef/at id (ef/append (ef/html [:div content]))))

(defn send-message
  "Sends a message to the request address."
  [message]
  (eb/publish @eb "demo.request" message))

(defn attach-listeners
  "Attaches listeners to both the the request and response addresses,
   displaying the received messages in the appropriate divs."
  []
  (eb/on-message @eb "demo.request" (partial append-content "#sent"))
  (eb/on-message @eb "demo.response" (partial append-content "#rcvd")))

(defn attach-send-click
  "Attaches handler to send a message when the send button is clicked."
  []
  (ef/at "#send-message"
         (events/listen :click
                        #(send-message (ef/from "#message" (ef/get-prop :value))))))
(defn init []
  (open-eventbus attach-listeners)
  (attach-send-click))

(set! (.-onload js/window) init)


