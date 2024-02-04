def gv

pipeline {
    agent any

    parameters {
        // string(name: 'VERSION', defaultValue: '', description: 'verison to deploy on prod')
        choice(name: 'VERSION', choices: ['1.1.0', '1.2.0', '1.3.0'], description: '')
        booleanParam(name: 'executeTests', defaultValue: true, description: '')
    }

    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }

        stage("build") {
            steps {
                script {
                    gv.buildApp()
                }
            }
        }

        stage("test") {
            when {
                expression {
                    params.executeTests
                }
            }

            steps {
                script {
                    gv.testApp()
                }
            }
        }

        stage("deploy") {
            steps {
                input {
                    message "Select the environment to deploy"
                    ok "Environment selected"
                    parameters {
                        choice(name: 'ENVIRONMENT', choices:['dev', 'test', 'prod'], description: '')
                    }
                }

                script {
                    gv.deployApp()
                    echo "Deploying to ${ENVIRONMENT}"
                }
            }
        }
    }
}
