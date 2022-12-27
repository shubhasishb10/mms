// Jenkins file for automatic deployment
pipeline {
    agent any
    stages {
        stage("branch") {
            steps {
                bat "echo 'selected branch name'"
                bat "echo ${branch}"
            }
        }
        stage ("cd") {
            steps {
                bat "cd D:/jenkins/temp/codebuild"
                bat "echo 'Current Directory'"
                bat "cd"
                bat "git checkout -f ${branch}"
            }
        }
        stage ("build") {
            steps {
                bat "mvn clean install"
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
    }
}