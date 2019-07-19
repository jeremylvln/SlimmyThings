pipeline {
    agent { docker { image 'openjdk:8' } }
    stages {
        stage('Build') {
            steps {
                sh 'env'
                sh 'chmod +x ./gradlew'
                sh './gradlew build --refresh-dependencies --stacktrace'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'build/libs/*.jar'
        }
    }
}