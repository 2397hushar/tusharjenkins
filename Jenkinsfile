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
                        mvn test -Dtest=TestRunner -Dcucumber.filter.tags="@Regression"
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
            
            // Publish HTML reports
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/extent-reports',
                reportFiles: '*.html',
                reportName: 'Extent Report'
            ])
        }
        success {
            echo "🎉 BUILD SUCCESSFUL!"
            emailext(
                subject: "Build Success: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "The build completed successfully.\n\nCheck reports at: ${env.BUILD_URL}",
                to: "tusharsangale2015@gmail.com"
            )
        }
        failure {
            echo "❌ BUILD FAILED!"
            emailext(
                subject: "Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "The build failed. Please check the logs at: ${env.BUILD_URL}",
                to: "tusharsangale2015@gmail.com"
            )
        }
    }
}
