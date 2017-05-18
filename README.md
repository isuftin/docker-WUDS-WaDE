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

### Building the containers

Before launching the containers, you will need to first build them on your local
system. It is easiest to do so using Docker Compose.

#### __Jenkins Container__

Included is a Jenkins Docker container. This container serves as a reference implementation
of a Jenkins job that creates an RPM for the WaDE project. In the future, this will
also be expanded to deploy the created binary and source RPMs to an Artifactory
RPM repository.

In order to properly run the Jenkins job, you will need to define GITHUB_ACCESS_TOKEN
in `jenkins/compose.env`. This GitHub access token is provided by GitHub in order
for you to access https://github.com/USGS-CIDA/postgres-fullapp

Once everything is in place, the Jenkins container may be launched by running the
command `docker-compose up jenkins`

When the Jenkins container is running, you should be able to log into it using
the username `jenkins` and password `jenkins` and run the `WADE_WEB_RPM_BUILD` job.

#### __Database Container__

The database container, when building, will use a database backup file that is provided
by the WaDE project and not included in this repository. The file should be named
wade.backup and placed into the database subdirectory in this project.

Once placed, the database container may be built by running the command
`docker-compose up database`

#### __Web Container__

The web container, when building, will use an RPM to install the WaDE website
onto an Apache server. The RPM is provided by the WaDE project. In the future, it
will be available through a public artifact repository. The RPM may also be built
by running the Vagrantfile [here](https://github.com/USGS-CIDA/WUDS_WaDE_OVERLAY/blob/master/rpm_build/Vagrantfile).
Information about that process can be found [here](https://github.com/USGS-CIDA/WUDS_WaDE_OVERLAY#vagrantfile). This requires
you to have access to the source GitHub repository for the original WaDE source code.

Once you have the RPM, it should be placed into web/src/wade-web.rpm

The Docker container may then be built by running the command `docker-compose build web`

### Launching the containers

Included is a Docker Compose configuration file which allows you to orchestrate
the starting and interactivity of the two containers. Simply execute:

`docker-compose up` (`docker-compose up -d` to run in daemon mode).

If this is your first run, Docker will need to build the containers before running them.
This may take some time depending on your network connection as well as the speed
of your computer. More information about building individual containers is found above.

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
