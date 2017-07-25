(defproject ronpa "0.1.0-SNAPSHOT"
  :description "Search words from Wikipedia."
  :url "https://github.com/CORDEA/ronpa"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.cli "0.3.5"]
                 [clj-http "3.6.1"]]
  :main ^:skip-aot ronpa.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
