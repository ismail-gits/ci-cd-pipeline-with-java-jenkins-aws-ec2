#!usr/bin/env groovy


// @Library('jenkins-shared-library') // If library is referenced globally in system config of Jenkins

library identifier: 'jenkins-shared-library@main', retriever: modernSCM(
    [$class: 'GitSCMSoruce',
    remote: 'https://github.com/ismail-gits/jenkins-shared-library.git',
    credentialsId: 'docker-credentials']
)

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