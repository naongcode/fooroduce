pipeline {
    agent any

    stages {
        stage('Upload JAR to EC2') {
            steps {
                script {
                    // 변수 정의
                    def jarFile = "C:/Users/user/Desktop/project/fooroduce/build/libs/fooroduce-1.0-SNAPSHOT.jar"
                    def ec2User = "ubuntu"
                    def ec2Host = "43.203.3.27"
                    def ec2Key = "C:/Users/user/.ssh/id_rsa"
                    def knownHostsPath = "C:/Users/user/.ssh/known_hosts"
                    def remotePath = "/home/ubuntu/fooroduce/fooroduce-1.0-SNAPSHOT.jar"

                    // EC2 호스트 키를 known_hosts에 추가
                    bat """
                    "C:/Program Files/Git/usr/bin/ssh-keyscan.exe" -H ${ec2Host} >> ${knownHostsPath}
                    """

                    // JAR 업로드
                    bat """
                    "C:/Program Files/Git/usr/bin/scp.exe" -i "${ec2Key}" -o UserKnownHostsFile=${knownHostsPath} "${jarFile}" "${ec2User}@${ec2Host}:${remotePath}"
                    """

                    // EC2에서 기존 프로세스 종료 및 새 JAR 실행
                    bat """
                    "C:/Program Files/Git/usr/bin/ssh.exe" -i "${ec2Key}" -o UserKnownHostsFile=${knownHostsPath} "${ec2User}@${ec2Host}" "pkill -f 'java -jar'"
                    "C:/Program Files/Git/usr/bin/ssh.exe" -i "${ec2Key}" -o UserKnownHostsFile=${knownHostsPath} "${ec2User}@${ec2Host}" "nohup java -jar ${remotePath} > /home/ubuntu/app.log 2>&1 &"
                    """
                }
            }
        }
    }
}
