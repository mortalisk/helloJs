(defproject xxxx "1.0.1"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring "1.3.2"]
                 [http-kit "2.1.16"]]
  ; allow lein run to find a place to start
  :main server.core)