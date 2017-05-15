# WaDE Docker

This project provides a containerized [WaDE Portal](http://wade.westernstateswater.org/)
built on top of the official community Docker containers for [PostgreSQL](https://hub.docker.com/_/postgres/)
and [PHP using Apache](https://hub.docker.com/_/php/).

## Environment variables

There are a few environment variables that, if set on the host system before running
Docker, will inject into the container to mutate configuration on the container.

- POSTGRES_USER: Sets the username for the postgres admin user. Default: postgres
- POSTGRES_PASSWORD: This sets the password for the postgres admin user for accessing the
	database. Default: postgres
- POSTGRES_DB: The name of the default database to create when creating the postgres
	container. You should probably not need to change this. Default: WADE

The Docker Compose configuration makes use of the compose.env file to pick up these
variables as a convenience.

If using the PHP container against a remote database, replace these variables with
your own connection information.

## Docker Compose

Included is a Docker Compose configuration file which allows you to orchestrate
the starting and interactivity of the two containers. Simply execute:

`docker-compose up` (`docker-compose up -d` to run in daemon mode).

If this is your first run, Docker will need to build the containers before running them.
This may take some time depending on your network connection as well as the speed
of your computer.

The database container should start first and the webserver will start afterwards.

You can check the health of the containers by running `docker ps` in another window.
You should see something that looks like:

```
CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS                       PORTS                                      NAMES
8a4ae9995413        wadedocker_web        "docker-php-entryp..."   31 seconds ago      Up 31 seconds (healthy)      0.0.0.0:80->80/tcp, 0.0.0.0:443->443/tcp   wade_web
b3f36359884b        wadedocker_database   "docker-entrypoint..."   About an hour ago   Up About an hour (healthy)   0.0.0.0:5432->5432/tcp                     wade_database
```

Once you see that the web server is showing up as healthy, you can point your browser
to [http://localhost/WADE/v0.2/GetCatalog/GetCatalog?loctype=HUC&loctxt=16020204&orgid=SAMPLE1&state=46](http://localhost/WADE/v0.2/GetCatalog/GetCatalog?loctype=HUC&loctxt=16020204&orgid=SAMPLE1&state=46)
to ensure everything works as expected. If you are running docker-machine, replace
localhost with the output of `docker-machine ip <name of your machine>`. Example:

```
$ docker-machine ip dev
192.168.99.100
```

## .htaccess

There is an .htaccess file that is included with this project. The file instructs
Apache to not require the `.php` suffix in URLs in order to call PHP files.

The configuration also rewrites content going to the client. Any content that
contains `http://localhost` is changed to a blank. The purpose of this is because
the database is seeded with link addresses going to `http://localhost/...`. This
only works when the Apache server is running directly on the host machine. When
changed to a blank, the link becomes relative and the links work regardless of
whether the server is on the host machine or a remote IP.

## config.inc.php

This file configures the WaDE project with a database connection string. The string
is created from the variables described above.
