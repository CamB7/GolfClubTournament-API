#!/usr/bin/env sh
# Minimal mvnw shim: prefer host mvn if available, otherwise instruct how to generate the real Maven Wrapper
if command -v mvn >/dev/null 2>&1; then
  mvn "$@"
else
  echo "mvn (Maven) not found. To generate the Maven Wrapper locally run:"
  echo "  mvn -N io.takari:maven:wrapper:wrapper"
  exit 1
fi
