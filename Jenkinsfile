pipeline {
    stages {
        stage('Build') {
            steps {
                sh 'env'
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