#!groovy

pipeline {
    agent any

    environment {
        JAVA_HOME = tool 'JDK 11'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        REMOTE_SERVER_IP = '192.168.88.82'
        REMOTE_SERVER_USERNAME = 'serg'
        JENKINS_HOME = '/var/lib/jenkins/workspace/'
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
                        sh "scp miracle-backend.tar docker-compose.yml ${REMOTE_SERVER_USERNAME}@${REMOTE_SERVER_IP}:/home/serg"
                    }
                }
            }
        }

        stage('Deploy on Remote Server with Docker Compose') {
            steps {
                script {
                    // Заходим на удаленный сервер и разворачиваем контейнер с использованием Docker Compose
                    sshagent(['your-ssh-credentials-id']) {
                        sh "ssh ${REMOTE_SERVER_USERNAME}@${REMOTE_SERVER_IP} 'cd /home/serg && docker load -i miracle-backend.tar && docker-compose -f ${DOCKER_COMPOSE_FILE} up -d'"
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