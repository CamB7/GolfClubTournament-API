IMAGE_NAME ?= golfclubtournament-api
TAG ?= latest
REGISTRY ?= camb7
RDS_HOST ?= golf-tournament-db.c11rtafvrn2t.us-east-1.rds.amazonaws.com
RDS_USER ?= admin
RDS_DB ?= golf_qap
DB_USER ?= root
DB_PASS ?= Keyin123

.PHONY: help build buildx push dump import-to-rds compose-up release

help:
	@echo "Makefile targets:"
	@echo "  make build           - Build Docker image (local single-arch)"
	@echo "  make buildx          - Build multi-arch image (requires docker buildx)"
	@echo "  make push            - Tag and push image to \'$(REGISTRY)\' (set REGISTRY)"
	@echo "  make dump            - Dump local MySQL database to /tmp/$(RDS_DB)_dump.sql"
	@echo "  make import-to-rds   - Import /tmp/$(RDS_DB)_dump.sql into RDS (set RDS_HOST and RDS_USER)"
	@echo "  make compose-up      - Start local docker compose (app + local db)"
	@echo "  make release         - build and push (release=build push)"

build:
	@echo "Building Docker image $(IMAGE_NAME):$(TAG)..."
	docker build -t $(IMAGE_NAME):$(TAG) .

buildx:
	@echo "Building multi-arch Docker image $(IMAGE_NAME):$(TAG) (linux/amd64,linux/arm64)..."
	docker buildx build --platform linux/amd64,linux/arm64 -t $(IMAGE_NAME):$(TAG) --load .

push:
	if [ "$(REGISTRY)" = "camb7" ]; then \
		echo "ERROR: set REGISTRY variable to your registry (e.g. myuser or ECR repo)"; exit 1; \
	fi
	@echo "Tagging and pushing to $(REGISTRY)/$(IMAGE_NAME):$(TAG)..."
	docker tag $(IMAGE_NAME):$(TAG) $(REGISTRY)/$(IMAGE_NAME):$(TAG)
	docker push $(REGISTRY)/$(IMAGE_NAME):$(TAG)

dump:
	@echo "Dumping local DB '$(RDS_DB)' to /tmp/$(RDS_DB)_dump.sql (user=$(DB_USER))"
	if [ -z "$(DB_PASS)" ]; then \
		echo "Please provide DB_PASS, e.g. make dump DB_PASS=Keyin123"; exit 1; \
	fi
	mysqldump -u $(DB_USER) -p$(DB_PASS) --databases $(RDS_DB) > /tmp/$(RDS_DB)_dump.sql
	@echo "Dump written to /tmp/$(RDS_DB)_dump.sql"

import-to-rds:
	@if [ "$(RDS_HOST)" = "golf-tournament-db.c11rtafvrn2t.us-east-1.rds.amazonaws.com" ]; then \
		echo "ERROR: set RDS_HOST and RDS_USER before running import-to-rds"; exit 1; \
	fi
	@test -f /tmp/$(RDS_DB)_dump.sql || (echo "Dump not found. Run 'make dump' first." && exit 1)
	@echo "Importing /tmp/$(RDS_DB)_dump.sql into RDS host $(RDS_HOST) database $(RDS_DB) as user $(RDS_USER)"
	mysql -h $(RDS_HOST) -u $(RDS_USER) -p -D $(RDS_DB) < /tmp/$(RDS_DB)_dump.sql

# Start compose for local development
compose-up:
	docker compose up --build

# Convenience: build and push
release: build push
	@echo "Release completed."

