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

def hazelDeployDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "hazel/Jenkinsfile")
def hazelDeployJob = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Deploy Hazelcast")
hazelDeployJob.definition = hazelDeployDefinition
hazelDeployJob.addProperty(new ParametersDefinitionProperty([
  new StringParameterDefinition("HAZEL_LICENSE", "changeme", "Hazelcast License"),
  new StringParameterDefinition("MAN_CENTER_HAZEL_LICENSE", "changeme", "Hazelcast Management Center License")
  ]))

def hazelDemoDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "hazel/Jenkinsfile-demo")
def hazelDemoJob = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Demo Hazelcast")
hazelDemoJob.definition = hazelDemoDefinition

def hazelCleanUpDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "hazel/Jenkinsfile-cleanup")
def hazelCleanUpJob = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Clean Up Hazelcast")
hazelCleanUpJob.definition = hazelCleanUpDefinition

def dockerBuildDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "Jenkinsfile")
def dockerBuildJob = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Build Docker Image of Jenkins")
dockerBuildJob.definition = dockerBuildDefinition
dockerBuildJob.addProperty(new ParametersDefinitionProperty([
  new StringParameterDefinition("USERNAME", "changeme", "Docker Hub Username"),
  new PasswordParameterDefinition("PW", "changeme", "Docker Hub Password")
  ]))

parent.reload()
