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

                    bat "start \"ZAP\" /B \"${zapHome}\\zap.bat\" --zap-url localhost -p 8800 --api-key hba1ge6kipfp41fdu7o7t0ul33 quick-scan -c WebGoat -u tester -s all --spider -r http://localhost:8080/WebGoat"
                    sleep(time: 10, unit: 'SECONDS')
                }
            }
        }        

    }
}
