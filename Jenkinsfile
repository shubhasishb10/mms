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
                        export CATALINA_HOME="D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64"
                        D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/bin/shutdown.bat
                        rmdir /s D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/webapps/demo-0.0.1-SNAPSHOT
                        del /f D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/webapps/demo-0.0.1-SNAPSHOT.war
                        export MAVEN_OPTS="-Xmx800m"
                        D:/maven3/bin/mvn clean install -e
                        echo 'Stopping existing running tomcat'
                        copy ./target/demo-0.0.1-SNAPSHOT.war D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/webapps/
                        D:/apache-tomcat-9.0.64-windows-x64/apache-tomcat-9.0.64/bin/startup.bat
                        echo 'Build and Deployment is completed'
                    """
            }
        }
    }
}