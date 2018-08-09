import jenkins.model.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
def jobName = "test-sample-cicd"
project = Jenkins.instance.createProject(WorkflowJob, jobName)
def script = '''
'''
def definition = new CpsFlowDefinition(script,false)
project.definition = definition
project.save()
