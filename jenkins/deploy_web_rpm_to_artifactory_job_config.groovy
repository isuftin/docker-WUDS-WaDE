import hudson.model.AbstractItem
import javax.xml.transform.stream.StreamSource
import jenkins.model.Jenkins

final jenkins = Jenkins.getInstance()

final itemName = 'DEPLOY_WEB_RPM_TO_ARTIFACTORY'
final configXml = new FileInputStream('/deploy_web_rpm_to_artifactory_job_config.xml')
final item = jenkins.getItemByFullName(itemName, AbstractItem.class)

if (item != null) {
  item.updateByXml(new StreamSource(configXml))
} else {
  jenkins.createProjectFromXML(itemName, configXml)
}
