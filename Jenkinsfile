#!groovy
//
//pipeline {
//    agent any
//
//    environment {
//        JAVA_HOME = tool 'JDK 11'
//        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
//        REMOTE_SERVER_IP = '192.168.88.82'
//        REMOTE_SERVER_USERNAME = 'serg'
//        JENKINS_HOME = '/var/lib/jenkins/workspace/'
//    }
//
//    stages {
//        stage('Checkout') {
//            steps {
//                checkout scm
//            }
//        }
//
//        stage('Build') {
//            steps {
//                script {
//                    sh 'mvn -B -DskipTests clean package'
//                }
//            }
//        }
//
//        stage('Build Docker Image') {
//            steps {
//                script {
//                    // Создание Docker-образа
//                    sh "docker build -t miracle-backend ."
//                }
//            }
//        }
//
//        stage('Save Docker Image') {
//            steps {
//                script {
//                    // Сохранение Docker-образа как архива
//                    sh "docker save -o miracle-backend.tar miracle-backend"
//                }
//            }
//        }
//
//        stage('Deploy to Remote Server') {
//            steps {
//                script {
//                    // Передача архива на удаленный сервер
//                    sshagent(['your-ssh-credentials-id']) {
//                        sh "scp miracle-backend.tar docker-compose.yml ${REMOTE_SERVER_USERNAME}@${REMOTE_SERVER_IP}:/home/serg"
//                    }
//                }
//            }
//        }
//
//        stage('Deploy on Remote Server with Docker Compose') {
//            steps {
//                script {
//                    // Заходим на удаленный сервер и разворачиваем контейнер с использованием Docker Compose
//                    sshagent(['your-ssh-credentials-id']) {
//                        sh "ssh ${REMOTE_SERVER_USERNAME}@${REMOTE_SERVER_IP} 'cd /home/serg && docker load -i miracle-backend.tar && docker-compose -f ${DOCKER_COMPOSE_FILE} up -d backend'"
//                    }
//                }
//            }
//        }
//    }
//
//    post {
//        success {
//            echo 'Deployment successful'
//        }
//
//        failure {
//            echo 'Deployment failed'
//        }
//    }


pipeline {
    agent any

    environment {
        JAVA_HOME = tool 'JDK 11'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        REMOTE_SERVER_IP = '192.168.88.82'
        REMOTE_SERVER_USERNAME = 'serg'
        JENKINS_HOME = '/var/lib/jenkins/workspace/'
        REMOTE_SERVER_SSH_CREDENTIALS = 'your-ssh-credentials-id'  // Идентификатор учетных данных для SSH-ключа
        DOCKER_IMAGE_TAG = 'miracle-backend:latest'
    }

//    stages {
//        stage('Build and Deploy on Remote Server') {
//            steps {
//                script {
//                    // Клонирование репозитория на удаленном сервере
//                    sshagent([REMOTE_SERVER_SSH_CREDENTIALS]) {
//                        sh "ssh ${REMOTE_SERVER_USERNAME}@${REMOTE_SERVER_IP} 'rm -rf /home/serg/backend && git clone https://github.com/Sadaaaaa/miracle-backend.git /home/serg/backend'"
//                    }
//
//                    sshagent([REMOTE_SERVER_SSH_CREDENTIALS]) {
//                        sh "scp docker-compose.yml ${REMOTE_SERVER_USERNAME}@${REMOTE_SERVER_IP}:/home/serg"
//                    }
//
//                    // Сборка и развертывание Docker на удаленном сервере
//                    sshagent([REMOTE_SERVER_SSH_CREDENTIALS]) {
//                        sh "ssh ${REMOTE_SERVER_USERNAME}@${REMOTE_SERVER_IP} 'docker-compose up --build -d'"
//                    }
//                }
//            }
//        }
//    }

    stages {
        stage('Get project from the Github') {
            steps {
                script {
                    sh "ssh ${REMOTE_SERVER_USERNAME}@${REMOTE_SERVER_IP} 'rm -rf /home/serg/backend && git clone https://github.com/Sadaaaaa/miracle-backend.git /home/serg/backend'"
                }
            }
        }

        stage('Build the new docker image') {
            steps {
                script {
                    sshagent([REMOTE_SERVER_SSH_CREDENTIALS]) {
                        // Сборка Docker-образа
                        sh "ssh ${REMOTE_SERVER_USERNAME}@${REMOTE_SERVER_IP} 'docker-compose stop && cd /home/serg/backend && docker build -t ${DOCKER_IMAGE_TAG} .'"
                    }
                }
            }
        }

        stage('Start docker-compose service') {
            steps {
                script {
                    // Сборка и развертывание Docker на удаленном сервере
                    sshagent([REMOTE_SERVER_SSH_CREDENTIALS]) {
                        sh "ssh ${REMOTE_SERVER_USERNAME}@${REMOTE_SERVER_IP} 'docker-compose stop && docker-compose up -d'"
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