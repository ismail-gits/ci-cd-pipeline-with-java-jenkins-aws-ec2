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
                script {
                    env.ENV = input message: 'Select the environment to deploy', ok: "Done", parameters: [choice(name: 'ENVIRONMENT1', choices:['dev', 'test', 'prod'], description: ''), choice(name: 'ENVIRONMENT2', choices:['dev', 'test', 'prod'], description: '')]

                    gv.deployApp()
                    echo "Deploying to ${ENV}"
                }
            }
        }
    }
}
