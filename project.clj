(defproject raffer "0.0.1-SNAPSHOT"
  :description "Twitter Raffle"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src" "src/raffer"] 

  :dependencies [[twitter-api "0.7.4"]
                 [org.clojure/clojure "1.5.1"]]

  :jvm-opts ["-Draffer.conf=./conf/raffer.conf"])
