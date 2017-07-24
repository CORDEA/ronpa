(ns ronpa.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json])
  (:gen-class))

(def ^:const url "https://ja.wikipedia.org/w/api.php?action=query&list=search&format=json&utf8=0")
(def ^:const search-query "&srsearch=")

(defn parse-json [json]
  (json/read-str json :key-fn keyword))

(defn request-url [query]
  (str url search-query query))

(defn request [query]
  (let [resp (client/get
               (request-url query))]
    (parse-json
      (:body resp))))

(defn get-results [json]
  (:search
    (:query json)))

(defn get-titles [lst]
  (map :title lst))

(defn output [lst]
  (doseq [it lst]
    (println it)))

(defn -main
  [& args]
  (if (= (count args) 1)
    (let [query (nth args 0)]
      (output
        (get-titles
          (get-results
            (request query)))))
    (throw (IllegalArgumentException. "Please specify the search query."))))
