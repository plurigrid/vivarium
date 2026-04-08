#!/bin/bash
# Deploy step-ca (Smallstep) on UDM SE for short-lived EAP-TLS certs
# Run this ON the UDM SE after enabling SSH
# Usage: ssh root@192.168.0.1 'bash -s' < deploy-step-ca.sh

set -euo pipefail

CONTAINER_NAME="step-ca"
CA_DIR="/data/step-ca"

mkdir -p $CA_DIR

# Pull step-ca
podman pull docker.io/smallstep/step-ca:latest

# Stop existing if any
podman stop $CONTAINER_NAME 2>/dev/null || true
podman rm $CONTAINER_NAME 2>/dev/null || true

# Initialize CA (first run only)
if [ ! -f "$CA_DIR/config/ca.json" ]; then
  podman run -it --rm \
    -v $CA_DIR:/home/step \
    smallstep/step-ca:latest \
    step ca init \
      --name "Vivarium BCI Factory CA" \
      --provisioner "admin" \
      --dns "192.168.0.1" \
      --address ":8443" \
      --deployment-type standalone
  echo "CA initialized. Root cert at $CA_DIR/certs/root_ca.crt"
  echo "IMPORTANT: Back up $CA_DIR/secrets/root_ca_key to YubiKey"
fi

# Run step-ca
podman run -d \
  --name $CONTAINER_NAME \
  --restart=always \
  -p 127.0.0.1:9443:8443 \
  -v $CA_DIR:/home/step \
  smallstep/step-ca:latest

echo "step-ca running on localhost:9443"
echo "Next: configure ACME provisioner for 24h cert TTL"
echo "  step ca provisioner add acme --type ACME --x509-max-dur 24h"
