pipeline {
	agent any
     environment {
      SEMGREP_APP_TOKEN = credentials('semgrep')
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
        
        stage('SAST-SONARQUBE') {
            steps{
                script{
					def scannerHome = tool 'sonar-local';
					withSonarQubeEnv() {
						bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectName=Webgoat -Dsonar.projectKey=WebgoatLast -Dsonar.java.binaries=target/classes"
					}
                }
            }
        }
        
        stage('Semgrep-Scan') {
            steps {
                  bat 'pip install semgrep'
                  bat 'semgrep ci'
            }
        }

        stage('TEST') {
			steps {
				bat 'mvn clean package'
			}
		}

        stage('BUILD') {
			steps {
				bat 'mvn clean install -DskipTests=true'
			}
		}
             
    }
}
