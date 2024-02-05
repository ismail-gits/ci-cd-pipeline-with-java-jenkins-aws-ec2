#!usr/bin/env groovy

@Library('jenkins-shared-library')
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

        stage('build-jar') {
            steps {
                script {
                    buildJar()
                }
            }
        }

        stage('build-push-image') {
            steps {
                script {
                   buildImage "ismailsdockers/java-maven-app:jma-3.0.1"
                   dockerLogin()
                   dockerPush "ismailsdockers/java-maven-app:jma-3.0.1"
                }
            }
        }

        stage('deploy-image') {
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
    }
}