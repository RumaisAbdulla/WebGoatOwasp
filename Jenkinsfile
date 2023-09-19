pipeline {
    agent any

    stages {
      
        stage('Build') {
            steps {
                        bat 'mvn package -DskipTests'
            }
        }
        
        stage('SonarQube Analysis') {
            steps{
                script{
                     def scannerHome = tool 'sonar-local';
                     withSonarQubeEnv() {
                    bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=WebgoatWebGoat -Dsonar.java.binaries=target/classes"
                    }
                }
            }
        }

        stage('OWASP Dependency-Check Vulnerabilities') {
      steps {
        dependencyCheck additionalArguments: '--scan ./ --format HTML', odcInstallation: 'dependency-check'
        dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
      }
    }
    }
}
