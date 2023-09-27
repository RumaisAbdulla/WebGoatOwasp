pipeline {
	agent any
    
	stages {
		
		stage('COMPILE') {
			steps {
				bat 'mvn clean compile'
			}
		}
        
		stage('OWASP DEPENDENCY CHECK') {
			steps {
				dependencyCheck additionalArguments:  '--scan ./', odcInstallation: 'dependency-check'
				dependencyCheckPublisher pattern: 'dependency-check-report.xml'
			}
		}        
    
        stage('TEST') {
			steps {
				bat 'mvn test'
			}
		}
        
        stage('SAST-SONARQUBE') {
            steps{
                script{
					def scannerHome = tool 'sonar-local';
					withSonarQubeEnv() {
						bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectName=WebgoatLast -Dsonar.projectKey=WebgoatLast -Dsonar.java.binaries=target/classes"
					}
                }
            }
        }

        stage('BUILD') {
			steps {
				bat 'mvn clean package'
			}
		}
        
        stage('DAST-ZAP') {
            steps {
                script {
                    def zapHome = 'C:\\Program Files\\OWASP\\Zed Attack Proxy'
                    def targetUrl = 'http://localhost:8080/WebGoat'
                    def reportDir = 'reports'

                    bat "start \"ZAP\" /B \"${zapHome}\\zap.bat\" -daemon -host 0.0.0.0 -port 8080 -config api.disablekey=true"
                    sleep(time: 30, unit: 'SECONDS')
                    bat "\"${zapHome}\\zap-api-scan.py\" -t ${targetUrl} -r \"${reportDir}\\zap-report.html\" -x \"${reportDir}\\zap-report.xml\""
                    bat "taskkill /IM java.exe /F"

                    archiveArtifacts artifacts: "${reportDir}/*", allowEmptyArchive: true
                }
            }
        }        

    }
}
