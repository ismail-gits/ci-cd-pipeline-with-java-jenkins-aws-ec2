#!usr/bin/env groovy

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

        stage('test-jar') {
            steps {
                script {
                    echo "Executing pipeline for branch $BRANCH_NAME"
                    gv.testJar()
                }
            }
        }

        stage('build-jar') {
            when {
                expression {
                    BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    gv.buildJar()
                }
            }
        }

        stage('build-image') {
            when {
                expression {
                    BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    gv.buildImage()
                }
            }
        }
    }
}
