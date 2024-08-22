---
id: docker_fat_installation
title: Docker Fat
description: Docker fat Installation Guide
image: https://img.shields.io/docker/image-size/frooodle/s-pdf/latest-fat?label=Stirling-PDF%20Fat
---
![Docker Image Size (tag)](https://img.shields.io/docker/image-size/frooodle/s-pdf/latest-fat?label=Stirling-PDF%20Fat)

## Docker Run

```bash
docker run -d \
  -p 8080:8080 \
  -v ./trainingData:/usr/share/tessdata \
  -v ./extraConfigs:/configs \
  -v ./logs:/logs \
  -e DOCKER_ENABLE_SECURITY=true \
  -e SECURITY_ENABLELOGIN=true \
  -e INSTALL_BOOK_AND_ADVANCED_HTML_OPS=true \
  -e LANGS=en_GB \
  --name stirling-pdf \
  frooodle/s-pdf:latest-fat
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
    image: frooodle/s-pdf:latest-fat
    ports:
      - '8080:8080'
    volumes:
      - ./trainingData:/usr/share/tessdata  # Required for extra OCR languages
      - ./extraConfigs:/configs
      - ./logs:/logs/
      # - ./customFiles:/customFiles/
    environment:
      - DOCKER_ENABLE_SECURITY=true
      - SECURITY_ENABLELOGIN=true
      - INSTALL_BOOK_AND_ADVANCED_HTML_OPS=false
      - LANGS=en_GB
```

!!! Note
    Podman is CLI-compatible with Docker, so simply replace "docker" with "podman".
