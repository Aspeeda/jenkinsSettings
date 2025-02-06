pipeline {
    agent { label 'maven' } 

    parameters {
        string(name: 'BROWSER', defaultValue: 'chrome', description: 'Выбор браузера (chrome/firefox)')
        string(name: 'BRANCH', defaultValue: 'main', description: 'Выбор ветки для тестов')
    }

    environment {
        GRADLE_OPTS = "-Dorg.gradle.daemon=false"
        ALLURE_RESULTS = "build/allure-results"
    }

    stages {
        stage('Checkout Repository') {
            steps {
                git branch: "${params.BRANCH}", 
                    url: 'https://github.com/Aspeeda/homework01.git',
                    credentialsId: 'jenkins'
            }
        }

        stage('Setup Dependencies') {
            steps {
                sh './gradlew dependencies'
            }
        }

        stage('Run UI Tests') {
            steps {
                sh "./gradlew clean test -Dbrowser=${params.BROWSER}"
            }
        }

        stage('Generate Allure Report') {
            steps {
                sh "./gradlew allureReport"
                archiveArtifacts artifacts: '**/build/allure-results/**', fingerprint: true
            }
        }
    }

    post {
        always {
            junit '**/build/test-results/**/*.xml'
            archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
        }

        failure {
            script {
                def message = "UI тесты упали! Проверь логи в Jenkins."
                sh "curl -X POST -H 'Content-Type: application/json' -d '{\"chat_id\": \"<TELEGRAM_CHAT_ID>\", \"text\": \"${message}\"}' https://api.telegram.org/bot<TELEGRAM_BOT_TOKEN>/sendMessage"
            }
        }
    }
}