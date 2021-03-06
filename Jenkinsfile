pipeline {
	//where and how to execute the Pipeline
	agent any
	
	options {
		buildDiscarder(logRotator(numToKeepStr: '5')) 
		disableConcurrentBuilds() 
	}
	
	//A section defining tools to auto-install and put on the PATH
	tools {
		jdk 'JDK_8'
		gradle 'Gradle'
	}
	
	triggers {
		pollSCM('@hourly')
	}
	
	stages{		
		stage('Checkout')
		{
			steps{
				echo "------------>Checkout<------------"
				checkout([$class: 'GitSCM', branches: [[name: 'master']], doGenerateSubmoduleConfigurations: false, extensions: [] , gitTool: 'GIT', submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'carlosbaezt', url: 'https://github.com/carlosbaezt/jenkins']]])
				bat 'gradlew.bat clean'	
			}
		}
		
		stage('Compile') {
			steps{
				echo "------------>Compile<------------"
				bat 'gradlew.bat --b ./build.gradle compileJava'
			}
		}
		
		stage('Unit Tests') {
			steps{
				echo "------------>Unit Tests<------------"
				bat 'gradlew.bat test --stacktrace --debug'
				junit '**/build/test-results/test/*.xml' //aggregate test results - JUnit
			}
		}
		
		stage('Static Code Analysis') {
			steps{
				echo '------------>Análisis de código estático<------------'
				withSonarQubeEnv('SonarQube') {
					bat "gradlew.bat sonarqube -Dsonar.projectKey=carlosbaezt_jenkins -Dsonar.organization=carlosbaezt-github  -Dsonar.host.url=https://sonarcloud.io   -Dsonar.login=a2de6572f1da8f3aa548373fc4d5e3c3a78f01f6"
				}
			}
		}
		
		stage('Build') {
			steps {
				echo "------------>Build<------------"
				bat 'gradlew.bat build -x test'
			}
		}
	}
	
	post {
		always {
			echo 'This will always run'
		}
		success {
			echo 'This will run only if successful'
		}
		failure {
			echo 'This will run only if failed'
			//send notifications about a Pipeline to an email
			mail (to: 'carlos.baez@ceiba.com.co',
			      subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
			      body: "Something is wrong with ${env.BUILD_URL}")
		}
		unstable {
			echo 'This will run only if the run was marked as unstable'
		}
		changed {
			echo 'This will run only if the state of the Pipeline has changed'
			echo 'For example, if the Pipeline was previously failing but is now successful'
		}
	}
}
