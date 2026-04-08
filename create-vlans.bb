#!/usr/bin/env bb
;; Create VLANs on UDM SE via UniFi API
;; Requires: UNIFI_USER and UNIFI_PASS env vars
;; Usage: UNIFI_USER=admin UNIFI_PASS=xxx bb create-vlans.bb

(require '[babashka.http-client :as http]
         '[cheshire.core :as json]
         '[babashka.fs :as fs])

(def base "https://192.168.0.1")
(def site "default")
(def user (or (System/getenv "UNIFI_USER") (throw (ex-info "Set UNIFI_USER" {}))))
(def pass (or (System/getenv "UNIFI_PASS") (throw (ex-info "Set UNIFI_PASS" {}))))

;; Login
(def cookie-file (str (fs/create-temp-file {:prefix "unifi-" :suffix ".txt"})))

(def login-resp
  (http/post (str base "/api/auth/login")
    {:headers {"Content-Type" "application/json"}
     :body (json/generate-string {:username user :password pass})
     :client (http/client {:ssl-context {:insecure true}})}))

(def cookies (get-in login-resp [:headers "set-cookie"]))

(def api (str base "/proxy/network/api/s/" site))

(def vlans
  [{:name "Trusted" :purpose "corporate" :vlan 10
    :ip_subnet "192.168.10.1/24" :dhcpd_enabled true
    :dhcpd_start "192.168.10.100" :dhcpd_stop "192.168.10.254"
    :dhcpd_dns_enabled true :dhcpd_dns_1 "192.168.0.1"}
   {:name "IoT" :purpose "corporate" :vlan 20
    :ip_subnet "192.168.20.1/24" :dhcpd_enabled true
    :dhcpd_start "192.168.20.100" :dhcpd_stop "192.168.20.254"
    :dhcpd_dns_enabled true :dhcpd_dns_1 "192.168.0.1"}
   {:name "Guest" :purpose "guest" :vlan 30
    :ip_subnet "192.168.30.1/24" :dhcpd_enabled true
    :dhcpd_start "192.168.30.100" :dhcpd_stop "192.168.30.254"
    :dhcpd_dns_enabled true :dhcpd_dns_1 "192.168.0.1"}])

(doseq [v vlans]
  (println (format "Creating VLAN %d (%s)..." (:vlan v) (:name v)))
  (http/post (str api "/rest/networkconf")
    {:headers {"Content-Type" "application/json" "Cookie" cookies}
     :body (json/generate-string v)
     :client (http/client {:ssl-context {:insecure true}})}))

(fs/delete cookie-file)
(println "VLANs created. Assign devices via UniFi controller.")
