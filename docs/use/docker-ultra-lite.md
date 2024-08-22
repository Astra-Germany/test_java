---
id: docker_ultra_lite_installation
title: Docker Ultra Lite
description: Docker Ultra Lite Installation Guide
image: https://img.shields.io/docker/image-size/frooodle/s-pdf/latest-ultra-lite?label=Stirling-PDF%20Ultra%20Lite
---
![Docker Image Size (tag)](https://img.shields.io/docker/image-size/frooodle/s-pdf/latest-ultra-lite?label=Stirling-PDF%20Ultra%20Lite)

## Docker Run

```bash
docker run -d \
  -p 8080:8080 \
  -v ./extraConfigs:/configs \
  -v ./logs:/logs \
  -e DOCKER_ENABLE_SECURITY=false \
  -e SECURITY_ENABLELOGIN=false \
  -e LANGS=en_GB \
  --name stirling-pdf \
  frooodle/s-pdf:latest-ultra-lite
```

!!! info
    Can also add these for customisation but are not required
    ```bash
    -v /location/of/customFiles:/customFiles \
    ```

## Docker Compose

```yml
services:
  stirling-pdf:
    image: frooodle/s-pdf:latest-ultra-lite
    ports:
      - '8080:8080'
    volumes:
      - ./extraConfigs:/configs
      - ./logs:/logs/
      # - ./customFiles:/customFiles/
    environment:
      - DOCKER_ENABLE_SECURITY=false
      - SECURITY_ENABLELOGIN=false
      - LANGS=en_GB
```

!!! Note
    Podman is CLI-compatible with Docker, so simply replace "docker" with "podman".
