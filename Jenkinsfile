void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/IamBlueSlime/SlimmyThings"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}

pipeline {
    agent { docker { image 'openjdk:8' } }
    stages {
        stage('Build') {
            steps {
                setBuildStatus("Build in progress", "PENDING");
                sh 'env'
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build --refresh-dependencies --stacktrace'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'build/libs/*.jar'
        }
        success {
            setBuildStatus("Build succeeded", "SUCCESS");
        }
        failure {
            setBuildStatus("Build failed", "FAILURE");
        }
    }
}