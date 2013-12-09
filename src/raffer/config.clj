(ns raffer.config
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io])
  (:import [java.util MissingResourceException]))

(defn resource [path]
  (when path
    (-> (Thread/currentThread) .getContextClassLoader (.getResource path))))

(def props
  (if-let [path (System/getProperty "raffer.conf")]
    (try
      (edn/read-string 
        (slurp (io/file (resource path))))
      (catch Exception e 
        (throw (IllegalArgumentException. 
                 (str "a path to raffer.conf \"" path "\" can't be found or have an invalid config (problem with the format?) " e)))))
    (throw (MissingResourceException. 
            "Raffer can't find a \"raffer.conf\" env variable that points to a configuration file (usually in a form of -Draffer.conf=<path>)"
            "" ""))))

(defn conf [& path]                  ;; e.g. (conf :datomic :url)
  (get-in props (vec path)))
