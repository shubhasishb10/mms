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
        stage ("build-and-deploy") {
            steps {
                bat """
                        cd D:/jenkins/temp/codebuild"
                        echo 'Current Directory'
                        cd
                        D:/git/bin/git checkout -f ${branch}
                        cd mms
                        D:/maven3/bin/mvn clean install -e -X
                        cd ..
                        echo 'Stopping existing running tomcat'
                        D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/bin/shutdown.bat
                        rmdir /s D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/webapps/demo-0.0.1-SNAPSHOT
                        del /f D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/webapps/demo-0.0.1-SNAPSHOT.war
                        copy ./target/demo-0.0.1-SNAPSHOT.war D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/webapps/
                        D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/bin/startup.bat
                        echo 'Build and Deployment is completed'
                        rmdir /s mms
                    """
            }
        }
    }
}