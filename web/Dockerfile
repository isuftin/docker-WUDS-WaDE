FROM php:7.1.4-apache

MAINTAINER Ivan Suftin <isuftin@usgs.gov>

ENV POSTGRES_USER postgres

ENV POSTGRES_PASSWORD postgres

ENV DATABASE_ADDRESS database

ENV POSTGRES_DB WADE

RUN apt-get update && \
	apt-get install -y php5-pgsql libpq-dev alien libpcre3-dev

RUN docker-php-ext-configure pgsql -with-pgsql=/usr/local/pgsql && \
	docker-php-ext-install pdo pdo_pgsql pgsql

RUN a2enmod rewrite
RUN a2enmod substitute

COPY root.crt /usr/lib/ssl/certs/local.crt
RUN	ln -sf /usr/lib/ssl/certs/local.crt /usr/lib/ssl/certs/`openssl x509 -hash -noout -in /usr/lib/ssl/certs/local.crt`.0
RUN echo "$(cat /usr/lib/ssl/certs/local.crt)" | tee -a /etc/ssl/certs/ca-certificates.crt

RUN curl -o /wade-web.rpm 'https://cida.usgs.gov/artifactory/rpm/wade-web/0.0.1/noarch/wade-web-0.0.1-1.el6.noarch.rpm'
RUN alien --version=0.2 /wade-web.rpm
RUN dpkg -i wade-web_0.0.1-2_all.deb && apt-get install -f
COPY ./src/.htaccess /var/www/html/.htaccess

HEALTHCHECK --interval=30s --timeout=2s \
	CMD curl -s -o /dev/null -I -w "%{http_code}" "http://localhost/WADE/v0.2/GetCatalog/GetCatalog?loctype=HUC&loctxt=16020204&orgid=SAMPLE1&state=46" | grep -q 200 || exit 1
