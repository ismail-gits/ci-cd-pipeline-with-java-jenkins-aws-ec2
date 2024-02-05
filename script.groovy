def incrementVersion() {
    echo "incrementing application version..."
    sh "mvn build-helper:parse-version versions:set \
        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVerion}.\\\${parsedVersion.nextIncrementalVersion} \
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

def buildImage() {
    echo "building the docker image..."
    sh "docker build -t ismailsdockers/java-maven-app/$IMAGE_VERSION ."
    echo "pushing the docker image to docker private repo..."

    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
        sh "echo $PASS | docker login -u $USER --password-stdin"
    }

    sh "docker push ismailsdockers/java-maven-app/$IMAGE_VERSION"
}

def deployImage() {
    echo "deploying docker image to AWS EC2..."
}

return this