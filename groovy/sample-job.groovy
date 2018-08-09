import jenkins.model.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
def jobName = "test-sample-cicd"
project = Jenkins.instance.createProject(WorkflowJob, jobName)
def script = '''
podTemplate(cloud: 'kcloud', label: 'main', containers: [
    containerTemplate(
        name: 'jnlp',
        image: 'diamanti/jnlp-slave'
    ),
    containerTemplate(
        name: 'git',
        image: 'diamanti/git',
        command: 'cat',
        ttyEnabled: true
    )],
    volumes: [persistentVolumeClaim(claimName: 'test-cicd-pvc', mountPath: '/data-cicd' )],
    annotations: [podAnnotation(key: "diamanti.com/endpoint0", value: '{"network":"blue","perfTier":"high"}' )]
    ) {
    node('main') {
        stage('Configure') {
          env.PATH = "${tool 'maven-3.3.9'}/bin:${env.PATH}"
        }
        stage('Checkout') {
          git 'https://github.com/bertjan/spring-boot-sample'
        }
        stage('Build') {
          sh 'mvn -B -V -U -e clean package'
        }
        stage('Archive') {
          junit allowEmptyResults: true, testResults: '**/target/**/TEST*.xml'
        }
    }
}
'''
def definition = new CpsFlowDefinition(script,false)
project.definition = definition
project.save()
