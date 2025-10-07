# See here for image contents: https://github.com/microsoft/vscode-dev-containers/tree/v0.245.2/containers/java/.devcontainer/base.Dockerfile

# [Choice] Java version (use -bullseye variants on local arm64/Apple Silicon): 11, 17, 11-bullseye, 17-bullseye, 11-buster, 17-buster
ARG VARIANT="21-bullseye"
FROM mcr.microsoft.com/devcontainers/java:${VARIANT}

RUN su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install java 21.0.8-tem"
RUN su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install scala 3.3.6"
RUN su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install sbt"
