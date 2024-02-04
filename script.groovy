def build_jar() {
    echo "building the application..."
    sh "mvn install"
}

def test_jar() {
    echo "testing the application..."
    sh "mvn test"
}

def build_image() {
    echo "build the docker image..."
    sh "docker build -t ismailsdockers/java-maven-app:jma-2.0.1 ."
}

def push_image() {
    echo "pushing the docker image to docker private repository..."
    
    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push ismailsdockers/java-maven-app:jma-2.0.1"
    }
}

return this
