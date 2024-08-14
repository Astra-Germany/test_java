# Main stage
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
        ca-certificates \
        tzdata \
        tini \
        bash \
        curl \
        shadow \
        su-exec \
        openssl \
        openssl-dev \
        openjdk21-jre \
# Doc conversion
        libreoffice \
# pdftohtml
        poppler-utils \
# OCR MY PDF (unpaper for descew and other advanced featues)
        ocrmypdf \
        tesseract-ocr-data-eng \
# CV
        py3-opencv \
# python3/pip
        python3 \
        py3-pip \
# Build tools
        build-base \
        make \
        gcc \
        g++ \
        musl-dev \
        linux-headers \
        clang-dev \
        git \
        cmake \
        zlib-dev \
        freetype-dev \
        jpeg-dev \
        libpng-dev \
        mesa-dev \
        glfw-dev \
        freeglut-dev \
        # Install additional packages for OpenGL
        mesa-gl \
        glfw \
        freeglut

# Clone and build MuPDF
WORKDIR /tmp
RUN git clone --recursive https://github.com/ArtifexSoftware/mupdf.git && \
    cd mupdf && \
    make -j $(nproc) && \
    make install

# Verify installation
RUN mupdf -version

# Clean up
RUN rm -rf /tmp/mupdf


# Setze Arbeitsverzeichnis
# WORKDIR /tmp
# RUN git clone https://github.com/pymupdf/PyMuPDF.git && \
# # Klone MuPDF-Repository
#     git clone --recursive https://github.com/ArtifexSoftware/mupdf.git && \
#     ls -la
# WORKDIR /

# # Setze Arbeitsverzeichnis für PyMuPDF
# WORKDIR /tmp/mupdf

# # Installiere MuPDF (Beispiel-Befehl, könnte je nach Makefile variieren)
# RUN make -j $(nproc) && make install

# # Setze Arbeitsverzeichnis für PyMuPDF
# WORKDIR /tmp/PyMuPDF

# # Installiere PyMuPDF
# RUN pip install .

# WORKDIR /
# RUN python3 /tmp/PyMuPDF/scripts/sysinstall.py --mupdf-dir /tmp/mupdf --pymupdf-dir /tmp/PyMuPDF

# uno unoconv and HTML
# Create virtual environment and install Python packages
RUN python3 -m venv /opt/venv && \
    . /opt/venv/bin/activate && \
    pip install --upgrade pip && \
    pip install --break-system-packages --no-cache-dir --upgrade unoconv WeasyPrint installer
#     PyMuPDF

RUN mv /usr/share/tessdata /usr/share/tessdata-original && \
    mkdir -p $HOME /configs /logs /customFiles /pipeline/watchedFolders /pipeline/finishedFolders && \
    fc-cache -f -v && \
    chmod +x /scripts/* && \
    chmod +x /scripts/init.sh && \
# User permissions
    addgroup -S stirlingpdfgroup && adduser -S stirlingpdfuser -G stirlingpdfgroup && \
    chown -R stirlingpdfuser:stirlingpdfgroup $HOME /scripts /usr/share/fonts/opentype/noto /configs /customFiles /pipeline && \
    chown stirlingpdfuser:stirlingpdfgroup /app.jar && \
    tesseract --list-langs

EXPOSE 8080/tcp

# Set user and run command
ENTRYPOINT ["tini", "--", "/scripts/init.sh"]
CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "/app.jar"]
