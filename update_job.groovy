timeout(5, timeUnit='MINUTES') {
    node('maven') {

        def config = readYaml text: $YAML_CONFIG
        def jenkinsUrl = config['JENKINS_URL']
        def login = config['JENKINS_USERNAME']
        def password = config['JENKINS_PASSWORD']

        stage('checkout') {
            checkout scm
        }

        stage('Update jobs') {
            sh "docker run -t localhost:5005/jenkins_updater $jenkinsUrl $login $password"
        }
    }
}