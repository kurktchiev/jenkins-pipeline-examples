import hudson.plugins.git.*;

def scm = new GitSCM("https://github.com/ebalsumgo/jenkins-pipeline-examples.git")
scm.branches = [new BranchSpec("*/master")];
def parent = Jenkins.instance

def hazelDeployDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "hazel/Jenkinsfile")
def hazelDeployJob = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Deploy Hazelcast")
hazelDeployJob.definition = hazelDeployDefinition

def hazelDemoDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "hazel/Jenkinsfile-demo")
def hazelDemoJob = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Demo Hazelcast")
hazelDemoJob.definition = hazelDemoDefinition

def hazelCleanUpDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "hazel/Jenkinsfile-cleanup")
def hazelCleanUpJob = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Demo Hazelcast")
hazelCleanUpJob.definition = hazelCleanUpDefinition

def dockerBuildDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "Jenkinsfile")
def dockerBuildJob = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Build Docker Image of Jenkins")
dockerBuildJob.definition = dockerBuildDefinition

parent.reload()
