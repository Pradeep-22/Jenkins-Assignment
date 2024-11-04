node {
    try {
        stage('Checkout') {
            echo "Starting checkout from GitHub"
            // Checkout code from GitHub
            git branch: 'main', url: 'https://github.com/Pradeep-22/Jenkins-Assignment.git'
        }

        stage('Build') {
            echo "Building the project using Maven"
            // Use Maven to build the project and create the WAR file
            sh 'mvn -f MyWebApp/pom.xml clean package'
        }

        stage('Deploy to Tomcat') {
            echo "Starting deployment to Tomcat server"
            // Copy WAR file to remote server
            sh '''
                scp -o StrictHostKeyChecking=no MyWebApp/target/MyWebApp.war ubuntu@3.109.213.56:/tmp/ || { echo "SCP failed"; exit 1; }
            '''
            // Move WAR file to Tomcat's webapps directory with sudo
            sh '''
                ssh -o StrictHostKeyChecking=no ubuntu@3.109.213.56 "sudo mv /tmp/MyWebApp.war /opt/apache-tomcat-9.0.96/webapps/" || { echo "SSH failed"; exit 1; }
            '''
            echo "Deployment to Tomcat completed"
        }
    } catch (Exception e) {
        echo "Error occurred: ${e.message}"
        currentBuild.result = 'FAILURE'
    } finally {
        if (currentBuild.result == 'SUCCESS') {
            echo "Deployment successful!"
        } else {
            echo "Deployment failed."
        }
    }
}

