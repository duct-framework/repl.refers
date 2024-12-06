(ns duct.repl.refers
  (:require [integrant.core :as ig]))

(defn- add-refers [ns-sym refers]
  (binding [*ns* (find-ns ns-sym)]
    (doseq [[alias sym] refers]
      (let [ns-sym   (symbol (namespace sym))
            name-sym (symbol (name sym))]
        (require ns-sym)
        (if (= alias name-sym)
          (refer ns-sym :only [name-sym])
          (refer ns-sym :only [name-sym] :rename {name-sym alias})))))
  (when-some [syms (seq (keys refers))]
    (prn (keyword (str ns-sym) "added") syms)))

(defn- remove-refers [ns-sym refers]
  (let [ns (find-ns ns-sym)]
    (doseq [[alias sym] refers]
      (when (identical? (resolve sym) (ns-resolve ns alias))
        (ns-unmap ns alias))))
  (when-some [syms (seq (keys refers))]
    (prn (keyword (str ns-sym) "removed") syms)))

(defn- identical-kvs [m1 m2]
  (reduce-kv (fn [m k v] (if (= v (m2 k ::miss)) (assoc m k v) m))
             {} m1))

(defmethod ig/init-key :duct.repl/refers [_ refers]
  (add-refers 'user refers)
  refers)

(defmethod ig/halt-key! :duct.repl/refers [_ refers]
  (remove-refers 'user refers))

(defmethod ig/suspend-key! :duct.repl/refers [_ _])

(defmethod ig/resume-key :duct.repl/refers [_ refers-new refers-old _]
  (let [common-refers  (identical-kvs refers-new refers-old)
        refers-deleted (remove (comp common-refers key) refers-old)
        refers-added   (remove (comp common-refers key) refers-new)]
    (remove-refers 'user (into {} refers-deleted))
    (add-refers    'user (into {} refers-added))
    refers-new))
