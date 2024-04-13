# Main stage
FROM alpine:20240329

# Copy necessary files
COPY scripts /scripts
COPY pipeline /pipeline
COPY src/main/resources/static/fonts/*.ttf /usr/share/fonts/opentype/noto
COPY src/main/resources/static/fonts/*.otf /usr/share/fonts/opentype/noto
COPY build/libs/*.jar app.jar

ARG VERSION_TAG


# Set Environment Variables
ENV DOCKER_ENABLE_SECURITY=false \
    VERSION_TAG=$VERSION_TAG \
    JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -XX:MaxRAMPercentage=75" \
	HOME=/home/stirlingpdfuser \
	PUID=1000 \
    PGID=1000 \
    UMASK=022

RUN apk update && apk upgrade

# JDK for app
# RUN echo "@testing https://dl-cdn.alpinelinux.org/alpine/edge/main" | tee -a /etc/apk/repositories
# RUN echo "@testing https://dl-cdn.alpinelinux.org/alpine/edge/community" | tee -a /etc/apk/repositories
# RUN echo "@testing https://dl-cdn.alpinelinux.org/alpine/edge/testing" | tee -a /etc/apk/repositories
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && apk update && \
    apk add tzdata \
    libreoffice
RUN apt-get -y install libreoffice --no-install-recommends --no-install-suggests
RUN apk add --no-cache ca-certificates
RUN apk add --no-cache tzdata 
RUN apk add --no-cache tini
RUN apk add --no-cache bash
RUN apk add --no-cache curl
RUN apk add --no-cache gcc
RUN apk add --no-cache musl
RUN apk add --no-cache libffi
RUN apk add --no-cache openjdk17-jre
RUN apk add --no-cache su-exec
RUN apk add --no-cache font-noto-cjk
RUN apk add --no-cache shadow
# Doc conversion
RUN apk update && apk add --no-cache libreoffice@testing
# pdftohtml
RUN apk add --no-cache poppler-utils
# OCR MY PDF (unpaper for descew and other advanced featues)
RUN apk add --no-cache ocrmypdf
RUN apk add --no-cache tesseract-ocr-data-eng
# CV
RUN apk add --no-cache py3-opencv
# python3/pip
RUN apk add --no-cache python3 && \
    wget https://bootstrap.pypa.io/get-pip.py -qO - | python3 - --break-system-packages --no-cache-dir --upgrade && \
# uno unoconv and HTML
    pip install --break-system-packages --no-cache-dir --upgrade unoconv WeasyPrint && \
    mv /usr/share/tessdata /usr/share/tessdata-original && \
    mkdir -p $HOME /configs /logs /customFiles /pipeline/watchedFolders /pipeline/finishedFolders && \
    fc-cache -f -v && \
    chmod +x /scripts/* && \
    chmod +x /scripts/init.sh && \
# User permissions
    addgroup -S stirlingpdfgroup && adduser -S stirlingpdfuser -G stirlingpdfgroup && \
    chown -R stirlingpdfuser:stirlingpdfgroup $HOME /scripts /usr/share/fonts/opentype/noto /configs /customFiles /pipeline && \
    chown stirlingpdfuser:stirlingpdfgroup /app.jar && \
    tesseract --list-langs

EXPOSE 8080

# Set user and run command
ENTRYPOINT ["tini", "--", "/scripts/init.sh"]
CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "/app.jar"]
