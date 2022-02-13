pipeline {
	agent any
	tools {
		maven 'Maven'
	}
	stages {
		stage('Test') {
			steps {	
				sh 'mvn clean test'
			}
			post {
				success {
					junit 'target/surefire-reports/**/*.xml' 
				}
			}
		}
		stage('Build') {
			steps {
				sh 'mvn clean package -DskipTests'
			}
		}
		stage('Undeploy') {
			steps {
				sh 'mvn tomcat7:undeploy -s /var/jenkins_home/settings.tomcat.xml'
			}
		}
		stage('Deploy') {
			steps {
				sh 'mvn tomcat7:deploy -s /var/jenkins_home/settings.tomcat.xml'
			}
		}
	}
}