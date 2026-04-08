#!/usr/bin/env bb
;; Deploy Pi-hole on UDM SE via podman
;; Run ON the UDM SE after enabling SSH
;; Usage: ssh root@192.168.0.1 'bb -' < deploy-pihole.bb

(require '[babashka.process :refer [shell]])

(def container-name "pihole")

(println "Pulling Pi-hole...")
(shell "podman" "pull" "docker.io/pihole/pihole:latest")

;; Stop existing if any
(shell {:continue true} "podman" "stop" container-name)
(shell {:continue true} "podman" "rm" container-name)

;; Run Pi-hole
(shell "podman" "run" "-d"
  "--name" container-name
  "--restart=always"
  "--network=host"
  "-e" "TZ=America/Los_Angeles"
  "-e" "WEBPASSWORD=changeme"
  "-e" "PIHOLE_DNS_=1.1.1.1;9.9.9.9"
  "-e" "DNSMASQ_LISTENING=all"
  "-v" "/data/pihole/etc:/etc/pihole"
  "-v" "/data/pihole/dnsmasq:/etc/dnsmasq.d"
  "pihole/pihole:latest")

(println "Pi-hole deployed. Web UI at http://192.168.0.1/admin")
(println "Set UDM DHCP DNS to 192.168.0.1 to activate for all clients.")
