FROM jenkins:2.46.2

MAINTAINER Ivan Suftin <isuftin@usgs.gov>

USER root

RUN apt-get update

RUN apt-get install -y rpm

COPY root.crt /usr/lib/ssl/certs/local.crt
RUN	ln -sf /usr/lib/ssl/certs/local.crt /usr/lib/ssl/certs/`openssl x509 -hash -noout -in /usr/lib/ssl/certs/local.crt`.0
RUN	/usr/bin/keytool -keystore $JAVA_HOME/jre/lib/security/cacerts -importcert -alias DOIRootCA.crt -file /usr/lib/ssl/certs/local.crt -storepass changeit -noprompt
RUN echo "$(cat /usr/lib/ssl/certs/local.crt)" | tee -a /etc/ssl/certs/ca-certificates.crt

USER jenkins

# Install plugins
COPY plugins.txt /plugins.txt
# This works until https://github.com/jenkinsci/docker/pull/365 is merged
RUN /usr/local/bin/install-plugins.sh $(cat /plugins.txt | tr '\n' ' ')

ENV JENKINS_SSH_PORT 0
ENV JENKINS_USER jenkins
ENV JENKINS_PASS jenkins
ENV GITHUB_ACCESS_TOKEN ""
ENV ARTIFACTORY_DEPLOY_USER ""
ENV ARTIFACTORY_DEPLOY_PASSWORD ""
ENV WADE_VERSION 0.0.1
ENV WADE_RELEASE 1
ENV WADE_EPOCH 1

COPY wade_web_rpm_build_job_config.xml /wade_web_rpm_build_job_config.xml
COPY deploy_web_rpm_to_artifactory_job_config.xml /deploy_web_rpm_to_artifactory_job_config.xml

# Copy in the scripts
COPY start_sshd.groovy /usr/share/jenkins/ref/init.groovy.d/1_start_sshd.groovy
COPY default_user.groovy /usr/share/jenkins/ref/init.groovy.d/2_default_user.groovy
COPY add_github_token.groovy /usr/share/jenkins/ref/init.groovy.d/3_add_github_token.groovy
COPY add_artifactory_credentials.groovy /usr/share/jenkins/ref/init.groovy.d/4_add_artifactory_credentials.groovy
COPY wade_web_rpm_build_job_config.groovy /usr/share/jenkins/ref/init.groovy.d/5_wade_web_rpm_build_job_config.groovy
COPY deploy_web_rpm_to_artifactory_job_config.groovy /usr/share/jenkins/ref/init.groovy.d/6_deploy_web_rpm_to_artifactory_job_config.groovy
