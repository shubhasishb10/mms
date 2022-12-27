pipeline {

    agent any

    stages {

        stage("Testing phase") {

            steps {

                bat "echo ${branch}"
            }
        }

        stage ("Change Directory") {

            steps {

                bat "cd D:/jenkins/temp/codebuild"
                bat "cd"
            }
        }
    }
}