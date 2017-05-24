import jenkins.model.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import org.jenkinsci.plugins.plaincredentials.*
import org.jenkinsci.plugins.plaincredentials.impl.*
import hudson.util.Secret
import hudson.plugins.sshslaves.*
import org.apache.commons.fileupload.*
import org.apache.commons.fileupload.disk.*
import java.nio.file.Files

domain = Domain.global()
store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
def env = System.getenv()

artifactoryCredentials = new UsernamePasswordCredentialsImpl(
  CredentialsScope.GLOBAL,
  "ARTIFACTORY_UPLOADER_CREDENTIALS", "Artifactory uploader user credentials Configuration",
  env['ARTIFACTORY_DEPLOY_USER'],
  env['ARTIFACTORY_DEPLOY_PASSWORD']
)



store.addCredentials(domain, artifactoryCredentials)
