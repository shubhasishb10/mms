pipeline {

    agent any

    stages {

        stage("Testing phase") {

            steps {

                sh "echo ${branch}"
            }
        }

        stage ("Change Directory") {

            steps {

                sh "cd D:/jenkins/temp/codebuild"
                sh "cd"
            }
        }
    }
}