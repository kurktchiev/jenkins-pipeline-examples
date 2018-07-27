import jenkins.model.*
import jenkins.util.SystemProperties
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.*
import org.csanchez.jenkins.plugins.kubernetes.*
import org.csanchez.jenkins.plugins.*

def env = System.getenv()
def jenkins = Jenkins.getInstance()

// Create Credentials first - Service Account
Credentials c = (Credentials) new ServiceAccountCredential(CredentialsScope.GLOBAL,'jenkins-sa-default',"")
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)

def serverUrl =  "https://" + env.KUBERNETES_SERVICE_HOST + ":" + env.KUBERNETES_SERVICE_PORT
def jenkinsUrl = "http://" + env.JENKINS_SERVICE_HOST + ":" + env.JENKINS_SERVICE_PORT

def kubernetes = new KubernetesCloud("kubernetes", null, serverUrl, env.JENKINS_NAMESPACE, jenkinsUrl, '10', 0, 0, 5)
def cacert = new File("/var/run/secrets/kubernetes.io/serviceaccount/ca.crt").getText('UTF-8')
kubernetes.setSkipTlsVerify(false)
kubernetes.setServerCertificate(cacert)

// Add credentials
kubernetes.setCredentialsId('jenkins-sa-default')

jenkins.clouds.replace(kubernetes)
