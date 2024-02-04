def gv

pipeline {
    agent any

    tools {
        maven 'maven-3.6'
    }

    stages {
        stage('build-jar') {
            steps {
                script {
                    echo "building the application..."
                    sh "mvn install"
                }
            }
        }

        stage('test-jar') {
            steps {
                script {
                    echo "testing the application..."
                    sh "mvn test"
                }
            }
        }

        stage('build-image') {
            steps {
                script {
                    echo "build the docker image..."
                    sh "docker build -t ismailsdockers/java-maven-app:1.1.3"
                }
            }
        }

        stage('pushing-image') {
            steps {
                script {
                    echo "pushing the docker image to docker private repository..."
    
                    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                        sh "echo ${PASS} | docker login -u ${USER} --password-stdin"
                        sh "docker push ismailsdockers/java-maven-app:1.1.3"
                    }
                }
            }
        }
    }
}