podTemplate(
  label: 'parent',
  containers:
  [
    containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:v1.8.12', command: 'cat', ttyEnabled: true),
    containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
  ],
  volumes: [hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')],
  serviceAccount: 'diamanti-jenkins-sa',
  namespace: 'jenkins')
{
  node ('parent')
  {
    def dockerBuild
    try
    {
      stage('Clone repository')
      {
        /* Let's make sure we have the repository cloned to our workspace */
        checkout scm
      }
      stage('Build the Jenkins Master Image')
      {
        container('docker')
        {
          sh 'docker login -u ${USERNAME} -p ${PW}'
          // dockerBuild = docker.build("diamanti/jenkins-master")
          sh 'docker build -t diamanti/jenkins-master .'
        }
      }
      stage('Push Jenkins Master Image to DockerHub')
      {
        container('docker')
        {
          // docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials')
          // {
          //   dockerBuild.push("diamanti/jenkins-master")
          // }
          sh 'docker push diamanti/jenkins-master'
        }
      }
    }
    finally
    {

    }
  }
}
