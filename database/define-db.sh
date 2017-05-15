#!/bin/bash
set -e

# dropdb -q -U "$POSTGRES_USER" "WADE"
psql -d "$POSTGRES_DB" -U "$POSTGRES_USER" -a -E -f /roles_create.sql

pg_restore -U "$POSTGRES_USER" -d "WADE" -v "/wade.backup" -w
