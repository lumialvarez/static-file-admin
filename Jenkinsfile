pipeline {
	agent any
	tools {
		maven 'Maven'
	}
	stages {
		stage('Get Version') {
			steps {
				script {
					MAVEN_VERSION = sh (
						script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
						returnStdout: true
					).trim()
				}
				script {
					currentBuild.displayName = "#" + currentBuild.number + " - v" + MAVEN_VERSION
				}
			}
		}
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
				sh 'mvn tomcat7:deploy -DskipTests -s /var/jenkins_home/settings.tomcat.xml'
			}
		}
	}
}