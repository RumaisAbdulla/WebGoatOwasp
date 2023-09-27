pipeline {
	agent any

    environment {
        ZAP_HOME = 'C:\Program Files\OWASP\Zed Attack Proxy'
        TARGET_URL = 'http://localhost:8080\WebGoat'
        REPORT_DIR = 'reports'
    }
	
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
            steps{
                script{
					def scannerHome = tool 'sonar-local';
					withSonarQubeEnv() {
						bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectName=WebgoatLast -Dsonar.projectKey=WebgoatLast -Dsonar.java.binaries=target/classes"
					}
                }
            }
        }        

    }
}
