#!/bin/bash
# Create VLANs on UDM SE via UniFi API
# Requires: UNIFI_USER and UNIFI_PASS env vars
# Usage: UNIFI_USER=admin UNIFI_PASS=xxx ./create-vlans.sh

set -euo pipefail

BASE="https://192.168.0.1"
SITE="default"

: "${UNIFI_USER:?Set UNIFI_USER}"
: "${UNIFI_PASS:?Set UNIFI_PASS}"

# Login
COOKIE=$(mktemp)
curl -sk -c "$COOKIE" "$BASE/api/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$UNIFI_USER\",\"password\":\"$UNIFI_PASS\"}"

API="$BASE/proxy/network/api/s/$SITE"

# Create VLAN 10 - Trusted
echo "Creating VLAN 10 (Trusted)..."
curl -sk -b "$COOKIE" "$API/rest/networkconf" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Trusted",
    "purpose": "corporate",
    "vlan": 10,
    "ip_subnet": "192.168.10.1/24",
    "dhcpd_enabled": true,
    "dhcpd_start": "192.168.10.100",
    "dhcpd_stop": "192.168.10.254",
    "dhcpd_dns_enabled": true,
    "dhcpd_dns_1": "192.168.0.1"
  }'

# Create VLAN 20 - IoT
echo "Creating VLAN 20 (IoT)..."
curl -sk -b "$COOKIE" "$API/rest/networkconf" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "IoT",
    "purpose": "corporate",
    "vlan": 20,
    "ip_subnet": "192.168.20.1/24",
    "dhcpd_enabled": true,
    "dhcpd_start": "192.168.20.100",
    "dhcpd_stop": "192.168.20.254",
    "dhcpd_dns_enabled": true,
    "dhcpd_dns_1": "192.168.0.1"
  }'

# Create VLAN 30 - Guest
echo "Creating VLAN 30 (Guest)..."
curl -sk -b "$COOKIE" "$API/rest/networkconf" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Guest",
    "purpose": "guest",
    "vlan": 30,
    "ip_subnet": "192.168.30.1/24",
    "dhcpd_enabled": true,
    "dhcpd_start": "192.168.30.100",
    "dhcpd_stop": "192.168.30.254",
    "dhcpd_dns_enabled": true,
    "dhcpd_dns_1": "192.168.0.1"
  }'

rm -f "$COOKIE"
echo "VLANs created. Assign devices via UniFi controller."
