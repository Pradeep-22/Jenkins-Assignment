node {
    try {
        stage('Checkout') {
            // Checkout code from GitHub
            git branch: 'main', url: 'https://github.com/Pradeep-22/Jenkins-Assignment.git'
        }

        stage('Build') {
            // Use Maven to build the project and create the WAR file
            sh 'mvn -f MyWebApp/pom.xml clean package'
        }

        stage('Deploy to Tomcat') {
            // Copy WAR file to remote server and move it to Tomcat webapps directory
            sh '''
                scp -o StrictHostKeyChecking=no MyWebApp/target/MyWebApp.war ubuntu@52.66.242.103:/tmp/
                ssh -o StrictHostKeyChecking=no ubuntu@52.66.242.103 "sudo mv /tmp/MyWebApp.war /opt/apache-tomcat-9.0.96/webapps/"
            '''
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
