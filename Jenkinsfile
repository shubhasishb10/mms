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
                        D:/git/bin/git checkout -f ${branch}
                        D:/git/bin/git pull
                        mvn clean install -e
                    """
            }
        }
    }
}
