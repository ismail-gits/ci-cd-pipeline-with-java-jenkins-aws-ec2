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

        stage('increment-version') {
            steps {
                script {
                    gv.incrementVersion()
                }
            }
        }

        stage('test-jar') {
            steps {
                script {
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

        stage('deploy-image') {
            when {
                expression {
                    gv.deployImage()
                }
            }
        }
    }
}