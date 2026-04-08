# Vivarium Network Inventory
## Discovered 2026-04-08

### Infrastructure (VLAN 10 — Trusted)

| Device | IP | MAC | Hostname | Notes |
|--------|-----|-----|----------|-------|
| UDM SE | 192.168.0.1 | 6c:63:f8:20:c0:9f | unifi.localdomain | Gateway. SSH off. Ports: 443,8443,8080,6789,8880 |
| Vivarium server | 192.168.0.9 | 6c:63:f8:49:01:2e | local.vivarium.sh | UniFi device. HTTP/HTTPS open |
| DGX (offline) | 192.168.0.136 | — | — | Last seen 2026-04-01 via Tailscale |

### Workstations (VLAN 10 — Trusted)

| Device | IP | MAC | Hostname |
|--------|-----|-----|----------|
| Bob's Mac | 192.168.0.107 | 6a:e4:46:60:45:98 | — |
| Mac | 192.168.0.13 | 8a:f5:37:d8:b5:ed | mac.localdomain |
| luka-1 | 192.168.0.15 | fc:b2:14:d8:63:e7 | luka-1.localdomain |
| ren MacBook Pro | 192.168.0.117 | f8:4d:89:8e:35:cf | ren-macbook-pro.localdomain |
| desktop-ptceb13 | 192.168.0.88 | ac:19:8e:05:fd:d9 | desktop-ptceb13.localdomain (Windows) |

### IoT / Smart Home (VLAN 20 — IoT)

| Device | IP | MAC | Hostname | Protocol | Notes |
|--------|-----|-----|----------|----------|-------|
| LG OLED77C5PUA | 192.168.0.103 | 64:75:da:49:e6:8e | lgwebostv.localdomain | SSAP/AirPlay | webOS 25. Ports: 3000,3001,7000,18181,36866 |
| Hue Bridge v2 | 192.168.0.163 | — | ecb5fabec998.local | Zigbee/Matter | BSB002. bridgeid=ecb5fafffebec998 |
| Apple Thread BR | 192.168.0.231 | — | Game-Room.local | Thread 1.3 | HomePod/AppleTV. Port 49153. DefaultDomain |
| HP Printer | — | — | — | _http._tcp | OfficeJet Pro 8120e [73B3E3] |
| Tapo C420S3 (3x) | — | — | — | Sub-1G | Battery 4K cameras. Need H200 hub |

### Physical Access (VLAN 20 — IoT)

| Device | IP | MAC | Hostname | Notes |
|--------|-----|-----|----------|-------|
| UniFi Access Hub | 192.168.0.33 | f4:e2:c6:1f:aa:f9 | uah-ent.localdomain | Enterprise hub |
| UniFi Intercom | 192.168.0.69 | 0c:ea:14:3d:8d:3c | ua-intercom.localdomain | Door intercom |

### Personal Devices (VLAN 30 — Guest or per-user)

| Device | IP | MAC | Hostname |
|--------|-----|-----|----------|
| iPhone | 192.168.0.6 | e2:15:be:eb:83:9f | iphone.localdomain |
| Watch | 192.168.0.32 | 42:06:a7:d4:5c:3b | watch.localdomain |

### Bluetooth (proximity from Bob's Mac)

| Device | Address | RSSI | Type |
|--------|---------|------|------|
| Adv360 Pro | F0:A9:10:CE:10:90 | connected | Kinesis split keyboard |
| hatchery | D2:BC:60:17:23:87 | -54 dBm | BLE beacon (unknown) |
| MX Master 3S | DF:3F:7B:06:9D:B9 | paired | Logitech mouse |
| Sennheiser MOMENTUM 4 | 80:C3:BA:81:F8:43 | paired | Headphones |
| OpenRun Pro 2 | A0:0C:E2:92:21:B2 | paired | Bone conduction |
| Audioengine HD3 | 00:22:D9:00:1B:9E | paired | Speakers |
| radhatter | 4C:2E:B4:1A:1B:44 | paired | Unknown BLE |

### Mesh Networks Active

| Protocol | Coordinator | Version | Coverage |
|----------|-------------|---------|----------|
| Thread | Apple BR @ .0.231 | 1.3.0 | Matter-native mesh |
| Zigbee | Hue Bridge @ .0.163 | — | Hue ecosystem |
| Sub-1G | (need Tapo H200) | — | Battery camera mesh |

### mDNS Services Broadcasting

`_screenpipe`, `_hue`, `_hap` (HomeKit), `_meshcop`/`_trel`/`_srpl-tls` (Thread), `_airplay`, `_raop`, `_companion-link`, `_ssh`, `_smb`, `_rfb`, `_remotepairing`, `_eppc`, `_ipps`/`_ipp` (printing)

### Total: 52 devices on en0 LAN, 57 ARP entries
