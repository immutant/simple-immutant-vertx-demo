(ns demo.web
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [hiccup.page :as page]
            [hiccup.form :as form]
            [immutant.web :as web]))

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

(defn start []
  (web/start app))
