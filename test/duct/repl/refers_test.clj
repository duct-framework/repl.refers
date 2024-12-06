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

(deftest test-init-suspend-resume
  (let [refers (ig/init-key :duct.repl/refers
                            {'ret-one `return-one
                             'return-two `return-two})
        ns     (find-ns 'user)]
    (is (= 1 ((ns-resolve ns 'ret-one))))
    (is (= 2 ((ns-resolve ns 'return-two))))
    (ig/suspend-key! :duct.repl/refers refers)
    (let [refers' (ig/resume-key :duct.repl/refers {'ret-one `return-one}
                                 refers refers)]
      (is (= {'ret-one `return-one} refers'))
      (is (= 1 ((ns-resolve ns 'ret-one))))
      (is (nil? (ns-resolve ns 'return-two))))))

(deftest test-prn-output
  (is (= (str ":user/added (ret-one return-two)\n"
              ":user/removed (ret-one return-two)\n")
         (with-out-str
           (let [refers (ig/init-key :duct.repl/refers
                                     {'ret-one `return-one
                                      'return-two `return-two})]
             (ig/halt-key! :duct.repl/refers refers))))))
