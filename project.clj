(defproject simple-immutant-vertx-demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2322"]
                 [io.vertx/clojure-api "1.0.3-SNAPSHOT"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.4"]
                 [enfocus "2.1.0"]]
  :plugins [[lein-cljsbuild "1.0.3"]]
  :immutant {:init demo.init/init
             :context-path "/"}
  :cljsbuild {:builds [{:source-paths ["src-cljs"]
                        :compiler {:output-to "resources/public/client.js"}}]}
  :repositories [["sonatype snapshots"
                  "https://oss.sonatype.org/content/repositories/snapshots/"]])
