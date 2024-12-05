(ns duct.repl.refers-test
  (:require [clojure.test :refer [deftest is testing]]
            [duct.repl.refers]
            [integrant.core :as ig]))

(defn return-one [] 1)
(defn return-two [] 2)

(deftest test-init-halt
  (let [refers (ig/init-key :duct.repl/refers
                            {'ret-one `return-one
                             'return-two `return-two})
        ns     (find-ns 'user)]
    (is (= 1 ((ns-resolve ns 'ret-one))))
    (is (= 2 ((ns-resolve ns 'return-two))))
    (ig/halt-key! :duct.repl/refers refers)
    (is (nil? (ns-resolve ns 'ret-one)))
    (is (nil? (ns-resolve ns 'return-two)))
    ;; halt again to test idempotency
    (ig/halt-key! :duct.repl/refers refers)))
