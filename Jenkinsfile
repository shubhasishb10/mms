// Jenkins file for automatic deployment
pipeline {
    agent any
    stages {
        stage("branch") {
            steps {
                echo 'selected branch name'
                echo ${branch}
            }
        }
        stage ("cd") {
            steps {
                cd "D:/jenkins/temp/codebuild"
                echo 'Current Directory'
                cd
                "D:/git/bin/git checkout -f ${branch}"
            }
        }
        stage ("build") {
            steps {
                cd mms
                "D:/maven3/bin/mvn clean install"
               cd ..
            }
        }
        stage ("clean") {
            steps {
                bat "echo 'Stopping existing running tomcat'"
                bat "D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/bin/shutdown.bat"
                bat "rmdir /s D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/webapps/demo-0.0.1-SNAPSHOT"
                bat "del /f D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/webapps/demo-0.0.1-SNAPSHOT.war"
            }
        }
        stage ("deploy") {
            steps {
                bat "copy ./target/demo-0.0.1-SNAPSHOT.war D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/webapps/"
                bat "D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/bin/startup.bat"
            }
        }
        stage ("complete") {
            steps {
                bat "echo 'Build and Deployment is completed'"
            }
        }
        stage ("cleanup") {
            steps {
                bat "rmdir /s mms"
            }
        }
    }
}