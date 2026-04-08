#!/usr/bin/env bb
;; Deploy step-ca (Smallstep) on UDM SE for short-lived EAP-TLS certs
;; Run ON the UDM SE after enabling SSH
;; Usage: ssh root@192.168.0.1 'bb -' < deploy-step-ca.bb

(require '[babashka.process :refer [shell]]
         '[babashka.fs :as fs])

(def container-name "step-ca")
(def ca-dir "/data/step-ca")

(fs/create-dirs ca-dir)

(println "Pulling step-ca...")
(shell "podman" "pull" "docker.io/smallstep/step-ca:latest")

(shell {:continue true} "podman" "stop" container-name)
(shell {:continue true} "podman" "rm" container-name)

;; Initialize CA (first run only)
(when-not (fs/exists? (str ca-dir "/config/ca.json"))
  (shell "podman" "run" "-it" "--rm"
    "-v" (str ca-dir ":/home/step")
    "smallstep/step-ca:latest"
    "step" "ca" "init"
    "--name" "Vivarium BCI Factory CA"
    "--provisioner" "admin"
    "--dns" "192.168.0.1"
    "--address" ":8443"
    "--deployment-type" "standalone")
  (println (str "CA initialized. Root cert at " ca-dir "/certs/root_ca.crt"))
  (println (str "IMPORTANT: Back up " ca-dir "/secrets/root_ca_key to YubiKey")))

;; Run step-ca
(shell "podman" "run" "-d"
  "--name" container-name
  "--restart=always"
  "-p" "127.0.0.1:9443:8443"
  "-v" (str ca-dir ":/home/step")
  "smallstep/step-ca:latest")

(println "step-ca running on localhost:9443")
(println "Next: configure ACME provisioner for 24h cert TTL")
(println "  step ca provisioner add acme --type ACME --x509-max-dur 24h")
