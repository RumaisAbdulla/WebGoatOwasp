pipeline {
	agent any
	
	stages {
		
		stage('COMPILE') {
			steps {
				bat 'mvn clean compile -DskipTests'
			}
		}
        
        stage('SONARQUBE ANALYSIS') {
            steps{
                script{
					def scannerHome = tool 'sonar-local';
					withSonarQubeEnv() {
						bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectName=WebgoatNew -Dsonar.projectKey=WebgoatNew -Dsonar.java.binaries=target/classes"
					}
                }
            }
        }
		
		stage('OWASP SCAN') {
			steps {
				dependencyCheck additionalArguments: '--scan ./ --format HTML', odcInstallation: 'dependency-check'
				dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
			}
		}
		
    }
}
