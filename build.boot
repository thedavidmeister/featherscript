(defn read-file   [file] (read-string (slurp file)))
(defn get-deps    []     (read-file "./dependencies.edn"))
;(defn get-devdeps []     (read-file "./dev_dependencies.edn"))

(set-env!
 :dependencies   (get-deps)
 :resource-paths #{"src"})

(require
 '[degree9.boot-semver :refer :all]
 '[degree9.boot-semgit :refer :all]
 '[degree9.boot-semgit.workflow :refer :all])

(task-options!
 pom    {:project 'degree9/featherscript
         :description "Feathersjs wrapper for ClojureScript."
         :url         "http://github.com/degree9/featherscript"
         :scm {:url "http://github.com/degree9/featherscript"}})

(deftask ci-deps
  "Force CI to fetch dependencies."
  []
  identity)

(deftask deploy
  "Build project for deployment to clojars."
  []
  (comp
    (version :minor 'inc :patch 'zero)
    (build-jar)
    (push-release)))

(deftask develop
  "Build project for local development."
  []
  (comp
    (version :develop true
             :minor 'inc
             :patch 'zero
             :pre-release 'snapshot)
    (watch)
    (build-jar)))
