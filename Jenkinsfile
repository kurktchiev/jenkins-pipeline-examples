pipeline {
  agent {
    kubernetes {
      cloud 'solutions1'
      label 'jenkins-image-builder'
      defaultContainer 'jnlp'
      yaml """
apiVersion: v1
kind: Pod
spec:
  serviceAccountName: sol-external-jenkins-diamanti-jenkins-sa
  containers:
  - name: docker
    image: docker:1.11
    command: ['cat']
    tty: true
    volumeMounts:
    - name: dockersock
      mountPath: /var/run/docker.sock
  volumes:
  - name: dockersock
    hostPath:
      path: /var/run/docker.sock
"""
    }
  }
  options {
    timestamps()
  }
  stages {
    stage('Build Jenkins Docker Image') {
      steps {
        git 'https://github.com/kurktchiev/jenkins-pipeline-examples.git'
        // updateGitlabCommitStatus name: 'docker-build-prod', state: 'pending'
        container('docker') {
          sh "docker login -u ${USERNAME} -p ${PW}"
          sh "docker build -t ${IMAGE} ."
          sh "docker tag ${IMAGE} ${IMAGE}:${TAG}.${BUILD_ID}"
        }
      }
      post {
        success {
          sh "echo Success"
          // updateGitlabCommitStatus name: 'docker-build-prod', state: 'success'
        }
        failure {
          sh "echo Failure"
          // updateGitlabCommitStatus name: 'docker-build-prod', state: 'failed'
        }
      }
    }
    stage('Push to Docker Hub') {
      steps {
        // updateGitlabCommitStatus name: 'docker-push-prod', state: 'pending'
        container('docker') {
          sh "docker push ${IMAGE}:${TAG}.${BUILD_ID}"
        }
      }
      post {
        success {
          sh "echo Success"
          // updateGitlabCommitStatus name: 'docker-push-prod', state: 'success'
        }
        failure {
          sh "echo Failure"
          // updateGitlabCommitStatus name: 'docker-push-prod', state: 'failed'
        }
      }
    }
  }
  post {
    success {
      sh "echo Success"
      // updateGitlabCommitStatus name: 'build-prod', state: 'success'
    }
    failure {
      sh "echo Failure"
      // updateGitlabCommitStatus name: 'build-prod', state: 'failed'
    }
    aborted {
      sh "echo Aborted"
      // updateGitlabCommitStatus name: 'build-prod', state: 'canceled'
    }
  }
}
