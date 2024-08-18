# Main stage
# FROM ludy87/pymupdf:latest
FROM alpine:3.20.2

# Copy necessary files
COPY scripts /scripts
COPY pipeline /pipeline
COPY src/main/resources/static/fonts/*.ttf /usr/share/fonts/opentype/noto/
#COPY src/main/resources/static/fonts/*.otf /usr/share/fonts/opentype/noto/
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


# JDK for app
RUN echo "@testing https://dl-cdn.alpinelinux.org/alpine/edge/main" | tee -a /etc/apk/repositories && \
    echo "@testing https://dl-cdn.alpinelinux.org/alpine/edge/community" | tee -a /etc/apk/repositories && \
    echo "@testing https://dl-cdn.alpinelinux.org/alpine/edge/testing" | tee -a /etc/apk/repositories && \
    apk upgrade --no-cache -a && \
    apk add --no-cache \
    musl-dev \
    jpeg-dev \
    zlib-dev \
    freetype-dev \
    clang \
    clang-dev \
    llvm \
    m4 \
    cmake \
    python3-dev \
    build-base \
    swig \
    git \
        ca-certificates \
        tzdata \
        tini \
        bash \
        curl \
        shadow \
        su-exec \
        mupdf-dev \
        openssl \
        openssl-dev \
        openjdk21-jre \
# Doc conversion
        libreoffice \
# pdftohtml
        poppler-utils \
# OCR MY PDF (unpaper for descew and other advanced features)
        ocrmypdf \
        tesseract-ocr-data-eng \
# CV
        py3-opencv \
# python3/pip
        python3 \
        py3-pip && \
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

# Erstelle und aktiviere ein virtuelles Umfeld
RUN <<EOF
    pip install --break-system-packages libclang
    wget https://github.com/pymupdf/PyMuPDF/archive/refs/tags/$PYMUPDF_VERSION.tar.gz
    tar -xvf $PYMUPDF_VERSION.tar.gz
    cd PyMuPDF-$PYMUPDF_VERSION
    PYMUPDF_SETUP_MUPDF_TESSERACT=0 PYMUPDF_SETUP_MUPDF_THIRD=0 PYMUPDF_MUPDF_LIB=/usr/lib/ python3 setup.py install
    cd ..
    rm -rf PyMuPDF-$PYMUPDF_VERSION $PYMUPDF_VERSION.tar.gz  # Clean up
EOF

RUN apk del \
    clang \
    clang-dev \
    llvm \
    m4 \
    cmake \
    build-base \
    python3-dev \
    swig \
    git

EXPOSE 8080/tcp

# Set user and run command
ENTRYPOINT ["tini", "--", "/scripts/init.sh"]
CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "/app.jar"]
