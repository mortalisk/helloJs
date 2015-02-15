(ns server.core
 (:use [ring.middleware.reload :as reload]
       [compojure.route :only [files not-found]]
       [compojure.handler :only [site]] ; form, query params decode; cookie; session, etc
       [compojure.core :only [defroutes GET POST DELETE ANY context]]
       org.httpkit.server
       clojure.stacktrace)
 (:gen-class))

(defn helloHandler [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "1"})

(defroutes all-routes
  (GET "/hello" [] helloHandler)
  (files "/")
  (not-found "<p>Page not found.</p>")) ;; all other, return 404

(defn -main [& args]
    (run-server (reload/wrap-reload (site #'all-routes)) {:port 8080}))
