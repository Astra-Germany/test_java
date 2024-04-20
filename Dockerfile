# Main stage
FROM alpine:20240329

# Copy necessary files
COPY scripts /scripts
COPY pipeline /pipeline
COPY src/main/resources/static/fonts/*.ttf /usr/share/fonts/opentype/noto
COPY src/main/resources/static/fonts/*.otf /usr/share/fonts/opentype/noto
COPY build/libs/*.jar /app.jar

ARG VERSION_TAG

# Set Environment Variables
ENV DOCKER_ENABLE_SECURITY=false \
    VERSION_TAG=$VERSION_TAG \
    JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -XX:MaxRAMPercentage=75" \
    HOME=/home/stirlingpdfuser \
    PUID=1000 \
    PGID=1000 \
    UMASK=022

# Add repositories and update indexes
RUN echo "@testing https://dl-cdn.alpinelinux.org/alpine/edge/main" | tee -a /etc/apk/repositories && \
    echo "@testing https://dl-cdn.alpinelinux.org/alpine/edge/community" | tee -a /etc/apk/repositories && \
    echo "@testing https://dl-cdn.alpinelinux.org/alpine/edge/testing" | tee -a /etc/apk/repositories && \
    apk update && \
    apk upgrade

# Install necessary packages
RUN apk add --no-cache ca-certificates tzdata tini bash curl openjdk17-jre su-exec font-noto-cjk shadow \
    libreoffice@testing poppler-utils ocrmypdf tesseract-ocr-data-eng py3-opencv python3

# Setup Python pip and install Python packages
RUN wget https://bootstrap.pypa.io/get-pip.py -qO - | python3 - --break-system-packages --no-cache-dir --upgrade && \
    python3 -m venv /venv && \
    source /venv/bin/activate && \
    pip install --no-cache-dir unoconv WeasyPrint

# Calibre Installation
RUN wget -nv -O- https://download.calibre-ebook.com/linux-installer.sh | sh /dev/stdin

# Cleanup unnecessary files
RUN rm -rf /var/cache/apk/* /tmp/*

# Create necessary directories and apply permissions
RUN mkdir -p $HOME /configs /logs /customFiles /pipeline/watchedFolders /pipeline/finishedFolders && \
    fc-cache -f -v && \
    chmod +x /scripts/* && \
    addgroup -S stirlingpdfgroup && adduser -S stirlingpdfuser -G stirlingpdfgroup && \
    chown -R stirlingpdfuser:stirlingpdfgroup $HOME /scripts /usr/share/fonts/opentype/noto /configs /customFiles /pipeline /app.jar

# Expose port
EXPOSE 8080

# Set user and run command
USER stirlingpdfuser
ENTRYPOINT ["tini", "--", "/scripts/init.sh"]
CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "/app.jar"]
