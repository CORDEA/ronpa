(ns ronpa.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def ^:const url "https://%s.wikipedia.org/w/api.php?action=query&list=search&srsearch=%s&format=json&utf8=")

(def cli-options
  [["-l" "--lang LANG" "language"
    :default "ja"]
   ["-h" "--help"]])

(defn parse-json [json]
  (json/read-str json :key-fn keyword))

(defn request-url [query lang]
  (format url lang query))

(defn request [query lang]
  (let [resp (client/get
               (request-url query lang))]
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

(defn run [query lang]
  (output
    (get-titles
      (get-results
        (request query lang)))))

(defn -main
  [& args]
  (let [opts (parse-opts args cli-options)]
    (let [lang (:lang (:options opts)) optargs (:arguments opts)]
      (if (= (count optargs) 1)
        (let [query (nth optargs 0)]
          (run query lang))
        (throw (IllegalArgumentException. "Please specify the search query."))))))
