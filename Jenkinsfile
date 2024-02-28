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
				dependencyCheck additionalArguments:  '--scan ./', odcInstallation: 'owaspdc'
				dependencyCheckPublisher pattern: 'dependency-check-report.xml'
			}
		}       
        
        stage('SAST-SONARQUBE') {
            steps{
                script{
					def scannerHome = tool 'sonarscanner';
					withSonarQubeEnv() {
						bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectName=Webgoat -Dsonar.projectKey=WebgoatLast -Dsonar.java.binaries=target/classes"
					}
                }
            }
        }
        
        stage('BUILD') {
			steps {
                bat 'mvn spotless:apply'
				bat 'mvn  clean install -DskipTests=true'
			}
		}
             
    }
}
