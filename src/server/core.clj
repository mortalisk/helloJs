(ns server.core
 (:use [ring.middleware.reload :as reload]
       [ring.util.response :only [get-header]]
       [compojure.route :only [files not-found]]
       [compojure.handler :only [site]] ; form, query params decode; cookie; session, etc
       [compojure.core :only [defroutes GET POST DELETE ANY context]]
       org.httpkit.server
       clojure.stacktrace)
 (:gen-class))
(def countUsers (atom 0))


(defn get-cookies [request]
  (if-let [cookiestr (get-header request "Cookie")]
    (let [cookie-array   (.split cookiestr " ")]
      (reduce 
        (fn [cookie-map cookie] 
          (let [key-val (.split cookie "=") 
                key     (nth key-val 0)
                val     (nth key-val 1)]
          (assoc cookie-map key val)))
        {}
        cookie-array))
    {}))

(defn get-cookie [request name]
  (get-in (get-cookies request) name))


(defn helloHandler [request]
  (if-let [id (get-cookie request "id")]
      {:status 200
       :body (str @countUsers)}
      (do
        (swap! countUsers inc)
        {:status 200
         :headers {"Content-Type" "text/plain" "Set-Cookie" (str "id=" @countUsers)}
         :body (str @countUsers)})))

(defroutes all-routes
  (GET "/hello" [] helloHandler)
  (files "/")
  (not-found "<p>Page not found.</p>")) ;; all other, return 404

(defn -main [& args]
    (run-server (reload/wrap-reload (site #'all-routes)) {:port 8080}))
