pipeline {
    agent any
    
    tools {
        maven 'maven_repo'
        jdk 'javaspace'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "✅ Code checked out"
            }
        }
        
        stage('Create Features Directory') {
            steps {
                bat '''
                    if not exist src\\test\\resources\\features mkdir src\\test\\resources\\features
                    echo "✅ Features directory created"
                '''
            }
        }
        
        stage('Clean') {
            steps {
                bat 'mvn clean -DskipTests'
            }
        }
        
        stage('Compile') {
            steps {
                bat 'mvn compile test-compile'
            }
        }
        
        stage('Test') {
            steps {
                script {
                    echo "=== Running Regression Tests ==="
                    bat '''
                        mvn test -Dtest=TestRunner -Dcucumber.filter.tags="@Regression" -Dheadless=true
                    '''
                }
            }
            post {
                always {
                    // Publish JUnit test results
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
    
    post {
        always {
            echo "=== Collecting Test Reports ==="
            echo "📊 Extent Report location: target/extent-reports/"
            
            // Archive artifacts (works without additional plugin)
            archiveArtifacts artifacts: 'target/extent-reports/**/*.html', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/surefire-reports/*.xml', allowEmptyArchive: true
        }
        success {
            echo "🎉 BUILD SUCCESSFUL!"
        }
        failure {
            echo "❌ BUILD FAILED!"
        }
    }
}
