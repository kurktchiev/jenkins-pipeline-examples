import hudson.plugins.git.*
import hudson.model.*
import jenkins.model.*
import jenkins.util.SystemProperties
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.*
import org.csanchez.jenkins.plugins.kubernetes.*
import org.csanchez.jenkins.plugins.*

def scm = new GitSCM("https://github.com/kurktchiev/jenkins-pipeline-examples.git")
scm.branches = [new BranchSpec("*/master")];
def parent = Jenkins.instance

//def hazelDeployParameters = []
def hazelDeployDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "hazel/Jenkinsfile")
def hazelDeployJob = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "WIP: Deploy Hazelcast")
hazelDeployJob.definition = hazelDeployDefinition

ParameterDefinition hazelLicenseParamDef = new StringParameterDefinition("HAZEL_LICENSE", "", "")
ParametersDefinitionProperty hazelLicenseParamsDef = new ParametersDefinitionProperty(hazelLicenseParamDef)
//hazelDeployParameters.add(hazelLicenseParamsDef)
ParameterDefinition hazelManCenterLicenseParamDef = new StringParameterDefinition("MAN_CENTER_HAZEL_LICENSE", "", "")
ParametersDefinitionProperty hazelManCenterLicenseParamsDef = new ParametersDefinitionProperty(hazelManCenterLicenseParamDef)
//hazelDeployParameters.add(hazelManCenterLicenseParamsDef)
//hazelDeployJob.addProperty(hazelDeployParameters)
hazelDeployJob.addProperty(hazelLicenseParamsDef)
hazelDeployJob.addProperty(hazelManCenterLicenseParamsDef)
hazelDeployJob.save()

def hazelDemoDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "hazel/Jenkinsfile-demo")
def hazelDemoJob = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Demo Hazelcast")
hazelDemoJob.definition = hazelDemoDefinition

def hazelCleanUpDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "hazel/Jenkinsfile-cleanup")
def hazelCleanUpJob = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Clean Up Hazelcast")
hazelCleanUpJob.definition = hazelCleanUpDefinition

def dockerBuildDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "Jenkinsfile")
def dockerBuildJob = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Build Docker Image of Jenkins")
dockerBuildJob.definition = dockerBuildDefinition
ParameterDefinition dockerParamDef = new PasswordParameterDefinition("PW", "", "")
ParametersDefinitionProperty dockerParamsDef = new ParametersDefinitionProperty(dockerParamDef)
dockerBuildJob.addProperty(dockerParamsDef)

parent.reload()
