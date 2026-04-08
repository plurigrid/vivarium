#!/bin/bash
# Deploy Pi-hole on UDM SE via podman
# Run this ON the UDM SE after enabling SSH
# Usage: ssh root@192.168.0.1 'bash -s' < deploy-pihole.sh

set -euo pipefail

PIHOLE_IP="192.168.0.2"
CONTAINER_NAME="pihole"

# Pull Pi-hole
podman pull docker.io/pihole/pihole:latest

# Stop existing if any
podman stop $CONTAINER_NAME 2>/dev/null || true
podman rm $CONTAINER_NAME 2>/dev/null || true

# Run Pi-hole
podman run -d \
  --name $CONTAINER_NAME \
  --restart=always \
  --network=host \
  -e TZ="America/Los_Angeles" \
  -e WEBPASSWORD="changeme" \
  -e PIHOLE_DNS_="1.1.1.1;9.9.9.9" \
  -e DNSMASQ_LISTENING="all" \
  -v /data/pihole/etc:/etc/pihole \
  -v /data/pihole/dnsmasq:/etc/dnsmasq.d \
  pihole/pihole:latest

echo "Pi-hole deployed. Web UI at http://192.168.0.1/admin"
echo "Set UDM DHCP DNS to 192.168.0.1 to activate for all clients."
