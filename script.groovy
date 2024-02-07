def incrementVersion() {
    echo "incrementing application version..."
    sh "mvn build-helper:parse-version versions:set \
        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
        versions:commit"
    def matcher = readFile("pom.xml") =~ "<version>(.+)</version>"
    def version = matcher[0][1]
    env.IMAGE_VERSION = "$version-$BUILD_NUMBER"
}

def testJar() {
    sh "mvn clean package"
    echo "Executing pipeline for branch $BRANCH_NAME"
    echo "testing the application..."
    sh "mvn test"
}

def buildJar() {
    echo "building the application..."
    // sh "mvn install"
}

def buildImage() {
    echo "building the docker image..."
    sh "docker build -t ismailsdockers/java-maven-app:$IMAGE_VERSION ."
    sh "docker tag ismailsdockers/java-maven-app:$IMAGE_VERSION ismailsdockers/java-maven-app:latest"
    echo "pushing the docker image to docker private repo..."

    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
        sh "echo $PASS | docker login -u $USER --password-stdin"
    }

    sh "docker push ismailsdockers/java-maven-app:$IMAGE_VERSION"
    sh "docker push ismailsdockers/java-maven-app:latest"
}

def deployImage() {
    echo "deploying docker image to AWS EC2..."


    // Deploying and running docker image as a single container
    // def dockerCmd = "docker run -d -p 8080:8080 ismailsdockers/java-maven-app:$IMAGE_VERSION"
    // sshagent(['EC2-server-key']) {
    //     sh "ssh -o StrictHostKeyChecking=no ec2-user@3.110.189.113 $dockerCmd"
    // }

    // Deploying and running image with postgres docker image using docker-compose (multiple containers)
    def dockerComposeCmd = "docker-compose -f docker-compose.yaml up --detach"
    sshagent(['EC2-server-key']) {
        sh "scp -o StrictHostKeyChecking=no docker-compose.yaml ec2-user@65.2.70.3:/home/ec2-user"
        sh "ssh -o StrictHostKeyChecking=no ec2-user@65.2.70.3 $dockerComposeCmd"
    }
}

def commitVerisonUpdate() {
    echo "committing version update to git repository..."

    sh 'git config --global user.email "jenkins@example.com"'
    sh 'git config --global user.name "jenkins"'

    sh "git status"
    sh "git branch"
    sh "git config --list"

    withCredentials([usernamePassword(credentialsId: 'gitlab-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
        sh "git remote set-url origin https://$USER:$PASS@gitlab.com/ismailGitlab/jenkins-pipeline-java-maven.git"
    }

    sh "git add ."
    sh "git commit -m 'jenkins-ci: version bump'"
    sh "git push origin HEAD:main"
}

return this
