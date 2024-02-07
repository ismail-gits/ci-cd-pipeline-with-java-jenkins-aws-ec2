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
    sh "mvn install"
}

def buildPushImage() {
    echo "building the docker image..."

    env.IMAGE_NAME="<docker-hub-repo-name>"
    sh "docker build -t $IMAGE_NAME:$IMAGE_VERSION ."
    sh "docker tag $IMAGE_NAME:$IMAGE_VERSION $IMAGE_NAME:latest"
    echo "pushing the docker image to docker private repo..."

    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
        sh "echo $PASS | docker login -u $USER --password-stdin"
    }

    sh "docker push $IMAGE_NAME:$IMAGE_VERSION"
    sh "docker push $IMAGE_NAME:latest"
}

def deployImageToAwsEc2() {
    echo "deploying docker image to AWS EC2..."


    // Deploying and running docker image as a single container
    // def dockerCmd = "docker run -d -p 8080:8080 ismailsdockers/java-maven-app:$IMAGE_VERSION"
    // sshagent(['EC2-server-key']) {
    //     sh "ssh -o StrictHostKeyChecking=no ec2-user@3.110.189.113 $dockerCmd"
    // }

    // Deploying and running image with postgres docker image using docker-compose (multiple containers)
    def shellCmd = "bash ./ec2-commands.sh '$IMAGE_NAME:$IMAGE_VERSION'"
    def ec2Instance = "<ec2-endpoint>"
    sshagent(['EC2-server-key']) {
        sh "scp -o StrictHostKeyChecking=no docker-compose.yaml $ec2Instance:/home/ec2-user"
        sh "scp -o StrictHostKeyChecking=no ec2-commands.sh $ec2Instance:/home/ec2-user"
        sh "ssh -o StrictHostKeyChecking=no $ec2Instance $shellCmd"
    }
}

def commitVerisonBump() {
    echo "committing version update to git repository..."

    sh 'git config --global user.email "jenkins@example.com"'
    sh 'git config --global user.name "jenkins"'

    sh "git status"
    sh "git branch"
    sh "git config --list"

    withCredentials([usernamePassword(credentialsId: 'gitlab-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
        sh "git remote set-url origin https://$USER:$PASS@gitlab.com/ismailGitlab/ci-cd-pipeline-with-java-jenkins-aws-ec2.git"
    }

    sh "git add ."
    sh "git commit -m 'jenkins-ci: version bump'"
    sh "git push origin HEAD:main"
}

return this
