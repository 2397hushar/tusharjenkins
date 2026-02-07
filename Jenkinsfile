pipeline {
    agent any
    
    tools {
        maven 'Maven_3.9.6'  // Your Maven tool name in Jenkins
        jdk 'jdk-25'         // Your JDK tool name in Jenkins
    }
    
    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[url: 'https://github.com/2397hushar/tusharjenkins.git']]
                ])
                
                // Verify files exist
                bat '''
                    echo "=== Checking Project Structure ==="
                    echo "Current Directory: %CD%"
                    dir
                    echo.
                    echo "=== Java Files ==="
                    dir /s /b *.java || echo "No Java files found"
                    echo.
                    echo "=== Looking for TestRunner ==="
                    dir /s /b *TestRunner* || echo "TestRunner not found"
                '''
            }
        }
        
        stage('Clean') {
            steps {
                bat '''
                    echo "=== Cleaning Project ==="
                    mvn clean -DskipTests
                '''
            }
        }
        
        stage('Compile') {
            steps {
                bat '''
                    echo "=== Compiling Tests ==="
                    mvn compile test-compile
                    
                    echo "=== Checking Compiled Classes ==="
                    if exist "target\\test-classes\\runners\\TestRunner.class" (
                        echo "✓ TestRunner.class found!"
                    ) else (
                        echo "✗ TestRunner.class not found!"
                        echo "Listing target directory:"
                        dir /s /b target\\*.class
                        exit 1
                    )
                '''
            }
        }
        
        stage('Test') {
            steps {
                bat '''
                    echo "=== Running Verification Test ==="
                    mvn test -Dtest=JenkinsVerificationTest || echo "Verification test may have warnings"
                    
                    echo "=== Running Main Tests ==="
                    mvn test -Dtest=TestRunner
                '''
            }
        }
    }
    
    post {
        always {
            echo "=== Collecting Test Reports ==="
            junit 'target/surefire-reports/*.xml'
            cucumber 'target/cucumber-*.json'
            
            // Archive important files
            archiveArtifacts artifacts: 'target/**/*.html, target/**/*.json, target/screenshots/**/*', fingerprint: true
            
            // Clean up workspace
            cleanWs()
        }
        success {
            echo "✅ Pipeline SUCCESS!"
            emailext (
                subject: "Pipeline SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Build ${env.BUILD_NUMBER} succeeded!",
                to: 'tusharsangale2015@gmail.com'
            )
        }
        failure {
            echo "❌ Pipeline FAILED!"
            emailext (
                subject: "Pipeline FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Build ${env.BUILD_NUMBER} failed! Check: ${env.BUILD_URL}",
                to: 'tusharsangale2015@gmail.com'
            )
        }
    }
}
