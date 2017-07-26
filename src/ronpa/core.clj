(ns ronpa.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def ^:const url "https://%s.wikipedia.org/w/api.php?action=query&list=search&srsearch=%s&srlimit=%d&format=json&utf8=")
(def ^:const warning "[WARNING] %s")

(def cli-options
  [["-l" "--lang LANG" "language"
    :default "ja"]
   ["-c" "--count COUNT" "number of results"
    :default 10
    :parse-fn #(Integer/parseInt %)
    :validate [#(<= 1 % 50) "Please specify between 1 and 50."]]
   ["-h" "--help"]])

(defn parse-json [json]
  (json/read-str json :key-fn keyword))

(defn request-url [query lang cnt]
  (format url lang query cnt))

(defn request [query lang cnt]
  (let [resp (client/get
               (request-url query lang cnt)
               {:cookie-policy :standard})]
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

(defn run [query lang cnt]
  (output
    (get-titles
      (get-results
        (request query lang cnt)))))

(defn show-opt-errors [lst]
  (binding [*out* *err*]
    (doseq [it lst]
      (println (format warning it)))))

(defn -main
  [& args]
  (let [opts (parse-opts args cli-options)]
    (show-opt-errors (:errors opts))
    (let [lang (:lang (:options opts))
          cnt (:count (:options opts))
          optargs (:arguments opts)]
      (if (= (count optargs) 1)
        (let [query (nth optargs 0)]
          (run query lang cnt))
        (throw (IllegalArgumentException. "Please specify the search query."))))))
