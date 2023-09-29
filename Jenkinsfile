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
        
        stage('DOCKER') {
			steps {
                    withDockerRegistry(credentialsId: '1969a5e8-8522-46bf-9f44-76d8458383b4', toolName: 'Docker') {
                        bat 'docker build -t webgoat:latest .'
                }
			}
		}
             
    }
}
