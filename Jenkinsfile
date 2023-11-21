pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        REMOTE_SERVER_IP = '192.168.88.82'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    sh 'mvn -B -DskipTests clean package'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Создание Docker-образа
                    sh "docker build -t miracle-backend ."
                }
            }
        }

        stage('Save Docker Image') {
            steps {
                script {
                    // Сохранение Docker-образа как архива
                    sh "docker save -o miracle-backend.tar miracle-backend"
                }
            }
        }

        stage('Deploy to Remote Server') {
            steps {
                script {
                    // Передача архива на удаленный сервер
                    sshagent(['your-ssh-credentials-id']) {
                        sh "scp miracle-app.tar ${REMOTE_SERVER_USERNAME}@${REMOTE_SERVER_IP}:/path/to/remote/directory/"
                    }
                }
            }
        }

        stage('Deploy on Remote Server with Docker Compose') {
            steps {
                script {
                    // Заходим на удаленный сервер и разворачиваем контейнер с использованием Docker Compose
                    sshagent(['your-ssh-credentials-id']) {
                        sh "ssh ${REMOTE_SERVER_USERNAME}@${REMOTE_SERVER_IP} 'cd /path/to/remote/directory/ && docker load -i miracle-app.tar && docker-compose -f ${DOCKER_COMPOSE_FILE} up -d'"
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment successful'
        }

        failure {
            echo 'Deployment failed'
        }
    }
}