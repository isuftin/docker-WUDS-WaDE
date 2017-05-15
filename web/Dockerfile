FROM php:7.1.4-apache

MAINTAINER Ivan Suftin <isuftin@usgs.gov>

ENV POSTGRES_USER postgres

ENV POSTGRES_PASSWORD postgres

ENV DATABASE_ADDRESS database

ENV POSTGRES_DB WADE

RUN apt-get update && \
	apt-get install -y php5-pgsql libpq-dev alien

RUN docker-php-ext-configure pgsql -with-pgsql=/usr/local/pgsql && \
	docker-php-ext-install pdo pdo_pgsql pgsql

RUN a2enmod rewrite
RUN a2enmod substitute

COPY wade-web.rpm /wade-web.rpm
RUN alien --version=0.2 /wade-web.rpm
RUN dpkg -i wade-web_0.0.1-2_all.deb && apt-get install -f
COPY ./src/.htaccess /var/www/html/.htaccess

HEALTHCHECK --interval=30s --timeout=2s \
	CMD curl -s -o /dev/null -I -w "%{http_code}" "http://localhost/WADE/v0.2/GetCatalog/GetCatalog?loctype=HUC&loctxt=16020204&orgid=SAMPLE1&state=46" | grep -q 200 || exit 1
