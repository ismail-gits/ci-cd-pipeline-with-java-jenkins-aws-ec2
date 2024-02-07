# Java Maven CI/CD Pipeline

This repository contains scripts and configurations for setting up a Continuous Integration and Continuous Deployment (CI/CD) pipeline for a Java Maven application. The pipeline is designed to automate the build, test, and deployment processes using Jenkins, Docker, and AWS EC2.

## Features

- **Increment Version:** Automatically increments the application version in the `pom.xml` file before each build.
- **Test Jar:** Executes Maven tests for the application.
- **Build Jar:** Builds the Java application jar file using Maven.
- **Build and Push Docker Image:** Builds a Docker image for the application and pushes it to a Docker private repository.
- **Deploy to AWS EC2:** Deploys the Docker image to an AWS EC2 instance using Docker Compose.
- **Commit Version Bump:** Commits the version update to the Git repository.

## Prerequisites

- Jenkins installed and configured with necessary plugins.
- Maven installed on the Jenkins server.
- Docker installed on the Jenkins server.
- AWS EC2 instance set up and configured for Docker deployment.
- GitLab credentials and Docker credentials configured in Jenkins.

## Usage

1. **Clone Repository:**
   ```bash
   git clone https://gitlab.com/ismailGitlab/jenkins-pipeline-java-maven.git

2. **Configure Jenkins:**
   - Install Jenkins and ensure it's properly configured with the provided `Jenkinsfile`.
   - Make sure Jenkins has access to necessary tools and credentials.

3. **Add Docker and AWS Credentials to Jenkins:**
   - Configure Jenkins with Docker and AWS credentials to enable Docker image pushing and AWS EC2 deployment.

4. **Run Jenkins Pipeline:**
   - Execute the Jenkins pipeline, specifying the appropriate branch (e.g., `main`), to trigger the CI/CD process.

5. **Access Deployed Application:**
   - Once the pipeline is successfully executed, access the deployed application via the public IP address of the AWS EC2 instance on port 8080.

## Directory Structure

- `script.groovy`: Contains Groovy scripts defining pipeline steps.
- `Jenkinsfile`: Jenkins pipeline configuration script.
- `Dockerfile`: Dockerfile for building the Docker image.
- `docker-compose.yaml`: Docker Compose configuration for deploying multiple containers.
- `ec2-commands.sh`: Bash script for deploying Docker image to AWS EC2.
- `src/main/java/com/example/Application.java`: Main Java application file.
- `src/test/java/AppTest.java`: JUnit test file.
- `pom.xml`: Maven project configuration file.
- `src/main/resources/static/index.html`: HTML file for displaying application status.