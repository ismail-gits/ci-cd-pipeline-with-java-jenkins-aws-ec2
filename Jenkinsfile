def gv

pipeline {
    agent any

    tools {
        maven 'maven-3.6'
    }

    stages {
        stage('init') {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }

        stage('testJar') {
            steps {
                script {
                    echo "Executing pipeline for branch $BRANCH_NAME"
                    gv.test_jar()
                }
            }
        }

        stage('buildJar') {
            when {
                expression {
                    BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    gv.build_jar()
                }
            }
        }

        stage('buildImage') {
            when {
                expression {
                    BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    gv.build_image()
                }
            }
        }

        stage('pushImage') {
            when {
                expression {
                    BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    gv.push_image()
                }
            }
        }
    }
}