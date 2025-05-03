pipeline {
    agent any

    environment {
        EC2_USER = "fooroduce"                // EC2 사용자
        EC2_IP = "43.203.3.27"         // EC2의 퍼블릭 IP 주소
        EC2_PATH = "/home/ubuntu/fooroduce.war" // EC2에 WAR 파일을 저장할 경로
        EC2_SSH_KEY = "~/.ssh/id_rsa"     // EC2에 접속할 SSH 키 경로
        WAR_FILE = "build/libs/fooroduce.war"   // 빌드된 WAR 파일 경로
    }

    stages {
        stage('Clone Repository') {
            steps {
                // GitHub에서 프로젝트 소스 코드를 클론
                git credentialsId: 'github_binary', url: 'https://github.com/naongcode/fooroduce.git'
            }
        }

        stage('Build WAR') {
            steps {
                // Gradle을 사용하여 WAR 파일 빌드
                sh './gradlew clean build'
            }
        }

        stage('Upload to EC2') {
            steps {
                script {
                    // SCP를 사용하여 WAR 파일을 EC2로 전송
                    sh "scp -i ${EC2_SSH_KEY} ${WAR_FILE} ${EC2_USER}@${EC2_IP}:${EC2_PATH}"
                }
            }
        }

        stage('Run on EC2') {
            steps {
                script {
                    // EC2에서 WAR 파일을 Docker 컨테이너에 복사하고 실행
                    sh "ssh -i ${EC2_SSH_KEY} ${EC2_USER}@${EC2_IP} 'docker cp ${EC2_PATH} bitcoin-app:/home/ubuntu/fooroduce.war && docker restart bitcoin-app'"
                }
            }
        }
    }

    post {
        failure {
            echo "❌ 배포 실패: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        }
        success {
            echo "✅ 배포 성공: http://${EC2_IP}:8080"
        }
    }
}
