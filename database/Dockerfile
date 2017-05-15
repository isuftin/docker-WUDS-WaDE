FROM postgres:9.6.2

MAINTAINER Ivan Suftin <isuftin@usgs.gov>

COPY wade.backup /wade.backup
COPY roles_create.sql /roles_create.sql
COPY define-db.sh /docker-entrypoint-initdb.d/define-db.sh

WORKDIR /

ENV LANG en_US.UTF-8

HEALTHCHECK --interval=2s --timeout=3s \
 CMD PGPASSWORD="${POSTGRES_PASSWORD}" | \
 	echo "SELECT 1;" | \
	psql -U "${POSTGRES_USER}" -w > /dev/null || exit 1
