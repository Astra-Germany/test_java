---
id: docker_installation
title: Docker Full
description: Docker Installation Guide
image: https://img.shields.io/docker/image-size/frooodle/s-pdf/latest?label=Stirling-PDF%20Full
---
![Docker Image Size (tag)](https://img.shields.io/docker/image-size/frooodle/s-pdf/latest?label=Stirling-PDF%20Full)

## Docker Run

```bash
docker run -d \
  -p 8080:8080 \
  -v ./trainingData:/usr/share/tessdata \
  -v ./extraConfigs:/configs \
  -v ./logs:/logs \
  -e DOCKER_ENABLE_SECURITY=false \
  -e INSTALL_BOOK_AND_ADVANCED_HTML_OPS=false \
  -e LANGS=en_GB \
  --name stirling-pdf \
  frooodle/s-pdf:latest
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
    image: frooodle/s-pdf:latest
    ports:
      - '8080:8080'
    volumes:
      - ./trainingData:/usr/share/tessdata  # Required for extra OCR languages
      - ./extraConfigs:/configs
      # - ./customFiles:/customFiles/
      # - ./logs:/logs/
    environment:
      - DOCKER_ENABLE_SECURITY=false
      - INSTALL_BOOK_AND_ADVANCED_HTML_OPS=false
      - LANGS=en_GB
```

!!! Note
    Podman is CLI-compatible with Docker, so simply replace "docker" with "podman".
