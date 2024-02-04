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
                    gv.build_jar()
                }
            }
        }

        stage('test-jar') {
            steps {
                script {
                    gv.test_jar()
                }
            }
        }

        stage('build-image') {
            steps {
                script {
                    gv.build_image()
                }
            }
        }

        stage('pushing-image') {
            steps {
                script {
                    gv.push_image()
                }
            }
        }
    }
}