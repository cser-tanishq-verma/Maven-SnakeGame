pipeline {
    agent any

    environment {
        IMAGE_NAME = "snake-game"
        CONTAINER_NAME = "snake-container"
    }
    stages {
        stage('Build Maven Project') {
            steps {
                bat 'mvn clean package'
            }
        }
        stage('Build Docker Image') {
            steps {
                bat 'docker build -t %IMAGE_NAME% .'
            }
        }
        stage('Remove Old Container') {
            steps {
                bat 'docker rm -f %CONTAINER_NAME% || exit 0'
            }
        }
        stage('Run Container') {
            steps {
                bat 'docker run -d --name %CONTAINER_NAME% %IMAGE_NAME%'
            }
        }
    }
    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}