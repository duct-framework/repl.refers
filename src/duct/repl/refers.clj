(ns duct.repl.refers
  (:require [integrant.core :as ig]))

(defmethod ig/init-key :duct.repl/refers [_ refers]
  (binding [*ns* (find-ns 'user)]
    (doseq [[alias sym] refers]
      (let [ns-sym   (symbol (namespace sym))
            name-sym (symbol (name sym))]
        (require ns-sym)
        (if (= alias name-sym)
          (refer ns-sym :only [name-sym])
          (refer ns-sym :only [name-sym] :rename {name-sym alias}))))))
