(defproject simple-immutant-vertx-demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1885"]
                 [cheshire "5.2.0" :exclusions [com.fasterxml.jackson.core/jackson-core]]
                 [io.vertx/clojure-api "0.2.0"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.4"]
                 [enfocus "2.0.0-beta1"]]
  :plugins [[lein-cljsbuild "0.3.2"]]
  :immutant {:init demo.init/init
             :context-path "/"
             :nrepl-port 5309}
  :cljsbuild {:builds [{:source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/client.js"}}]})
