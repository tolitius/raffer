(ns raffer
  (:require [raffer.config :refer [conf]]
            [clojure.java.io :refer [as-file]]
            [clojure.edn :as edn])
  (:use
   [twitter.oauth]
   [twitter.callbacks]
   [twitter.callbacks.handlers]
   [twitter.api.restful])
  (:import
   (twitter.callbacks.protocols SyncSingleCallback)))

(def my-creds (make-oauth-creds (conf :twitter :consumer-key)
                                (conf :twitter :consumer-secret)
                                (conf :twitter :access-token)
                                (conf :twitter :access-token-secret)))

(defn find-players [q & {:keys [max-id since-id]
                         :or {max-id (Long/MAX_VALUE)
                              since-id 0}}]
  (let [res (-> (search-tweets :oauth-creds my-creds 
                               :params {:q q :count 100 :max-id max-id :since-id since-id})
                (get-in [:body :statuses]))]
    (map (fn [{:keys [user id]}] 
           (let [{:keys [screen_name name max_id]} user] 
             {:screen_name screen_name
              :name name
              :id id})) res)))

(defn next-max-id [players]
  (when (seq players)
    (dec (apply min (map :id players)))))  ;; dec here not to get the same tweet in the next "page"

(defn next-since-id [players]
  (when (seq players)
    (apply max (map :id players))))

(defn up-to-now [q & {:keys [since-id]
                      :or {since-id 0}}]
  (let [page (find-players q :since-id since-id)]
    (loop [p page res []]
      (if (seq p)
        (recur (find-players q :max-id (next-max-id p) 
                               :since-id since-id)
               (concat res p))
        res))))

(defn scan-for [q]
  (let [players-path (str (conf :path :players) "." (hash q))   ;; making a somewhat unique "db" based on a query
        player-db (edn/read-string (if (.exists (as-file players-path))
                                                (slurp players-path)))
        since-id (or (next-since-id player-db) 0)
        new-players (up-to-now q :since-id since-id)]
    (when (seq new-players)
      (spit players-path (pr-str (concat player-db new-players))))))
