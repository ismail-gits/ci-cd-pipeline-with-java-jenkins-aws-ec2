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

        stage('build-push-image') {
            when {
                expression {
                    BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    gv.buildPushImage()
                }
            }
        }

        stage('deploy-to-aws-ec2') {
            when {
                expression {
                    BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    gv.deployImageToAwsEc2()
                }
            }
        }

        stage('commit-version-bump') {
            when {
                expression {
                    BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    gv.commitVerisonBump()
                }
            }
        }
    }
}
