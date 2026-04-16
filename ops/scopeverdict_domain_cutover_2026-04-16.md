# ScopeVerdict Domain Cutover - 2026-04-16

## Goal
Make `https://scopeverdict.com` the production canonical domain for the site.

## App setting
Set:

```bash
APP_BASE_URL=https://scopeverdict.com
```

This drives:
- canonical URLs
- JSON-LD website and organization URLs
- `robots.txt` sitemap URL
- `sitemap.xml` absolute URLs

## DNS
Point the purchased domain to the production server.

Typical options:
- apex/root domain `scopeverdict.com` -> `A` record to the VM public IP
- `www.scopeverdict.com` -> `CNAME` to `scopeverdict.com`

If the registrar uses ALIAS/ANAME/flattening for apex, use the platform-appropriate equivalent.

## Reverse proxy or web server
If Nginx or Caddy is in front of Spring Boot, make sure the hostnames include:
- `scopeverdict.com`
- optionally `www.scopeverdict.com`

Current OCI host note:
- existing Nginx config proxies `scopeverdict.com` to `http://127.0.0.1:8098`

Preferred behavior:
- serve the site on `scopeverdict.com`
- 301 redirect `www.scopeverdict.com` -> `https://scopeverdict.com$request_uri`

## Oracle VM systemd example
Add or update:

```ini
Environment=APP_BASE_URL=https://scopeverdict.com
```

Then restart the service.

## Post-cutover checks
After deploy, verify:

1. `https://scopeverdict.com/` loads
2. page source canonical uses `https://scopeverdict.com/...`
3. `https://scopeverdict.com/robots.txt` points to `https://scopeverdict.com/sitemap.xml`
4. `https://scopeverdict.com/sitemap.xml` contains only `https://scopeverdict.com/...` URLs
5. if `www` is enabled, it 301 redirects to apex

## Current code status
The app already supports production canonicalization through `APP_BASE_URL`.
Automated test coverage now verifies that when `APP_BASE_URL=https://scopeverdict.com`, canonical, schema, robots, and sitemap all follow the new domain.
