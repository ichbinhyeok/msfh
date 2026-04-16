# Oracle VM Persistent CSV Storage - 2026-04-14

## Goal
Keep CSV logs and lead data alive across:
- app restarts
- VM reboots
- GitHub Actions deploys that replace the app release directory

## Rule
Do not store runtime CSV data inside the app release folder.

Use a persistent absolute path on the Oracle VM such as:
- `/var/lib/mysafefloridahome/leads.csv`
- `/var/lib/mysafefloridahome/lead_events.csv`
- `/var/lib/mysafefloridahome/partner_inquiries.csv`

## Required environment variables
- `APP_STORAGE_LEADS_PATH=/var/lib/mysafefloridahome/leads.csv`
- `APP_STORAGE_EVENTS_PATH=/var/lib/mysafefloridahome/lead_events.csv`
- `APP_STORAGE_PARTNER_INQUIRIES_PATH=/var/lib/mysafefloridahome/partner_inquiries.csv`
- `APP_ADMIN_USERNAME=admin`
- `APP_ADMIN_PASSWORD=<strong-random-password>`
- `APP_BASE_URL=https://your-domain.com`

## One-time server setup
```bash
sudo mkdir -p /var/lib/mysafefloridahome
sudo chown -R ubuntu:ubuntu /var/lib/mysafefloridahome
chmod 750 /var/lib/mysafefloridahome
```

Replace `ubuntu` with the real service user on the VM.

## Why this survives restart
- The app writes CSV files to an absolute filesystem path.
- `LeadStorageService` creates parent directories and appends to the same files if they already exist.
- A service restart does not remove those files.

## GitHub Actions deploy rule
Deploy the jar or release files into a release directory such as:
- `/opt/mysafefloridahome/current`

But keep runtime CSV files outside that directory:
- `/var/lib/mysafefloridahome/...`

This way a deploy can replace `/opt/mysafefloridahome/current` without touching the CSV files.

## Systemd example
```ini
[Unit]
Description=My Safe Florida Home Verdict
After=network.target

[Service]
User=ubuntu
WorkingDirectory=/opt/mysafefloridahome/current
Environment=APP_BASE_URL=https://your-domain.com
Environment=APP_STORAGE_LEADS_PATH=/var/lib/mysafefloridahome/leads.csv
Environment=APP_STORAGE_EVENTS_PATH=/var/lib/mysafefloridahome/lead_events.csv
Environment=APP_STORAGE_PARTNER_INQUIRIES_PATH=/var/lib/mysafefloridahome/partner_inquiries.csv
Environment=APP_ADMIN_USERNAME=admin
Environment=APP_ADMIN_PASSWORD=change-this
ExecStart=/usr/bin/java -jar /opt/mysafefloridahome/current/mysafefloridahome-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```

## GitHub Actions deployment note
If the workflow copies a fresh jar to the VM, it should:
1. upload the new jar into the release directory
2. update the `current` symlink or replace only app files
3. restart the systemd service
4. never delete `/var/lib/mysafefloridahome`

## Minimum sanity check after deploy
```bash
ls -l /var/lib/mysafefloridahome
sudo systemctl restart mysafefloridahome
ls -l /var/lib/mysafefloridahome
```

If the timestamps and files remain, the storage path is persistent across restart.
