pipeline {
    agent any

    parameters {
        choice(name: 'TEST_SUITE', choices: [
            '.\\src\\test\\resources\\suites\\TestSuites.xml',
            '.\\src\\test\\resources\\suites\\TestSuites_Vietjet.xml'
        ], description: 'Select the test suite')
    }

    stages {
        stage('Build and Test') {
            steps {
                bat """
                    @echo off
                    call mvn clean test -DsuiteXmlFile=%TEST_SUITE%
                    if %ERRORLEVEL% NEQ 0 (
                        echo Maven test failed!
                        exit /b %ERRORLEVEL%
                    )
                """
            }
        }

        stage('Check Report Exists') {
            steps {
                bat """
                    if not exist target\\surefire-reports\\testng-results.xml (
                        echo TestNG report not found!
                        exit /b 1
                    )
                """
            }
        }

        stage('Parse Results') {
            steps {
                bat """
                    @echo off
                    setlocal enabledelayedexpansion

                    for /f "tokens=3,5,7,9,11 delims== " %%A in ('findstr "<testng-results" target\\surefire-reports\\testng-results.xml') do (
                        set "ignored=%%~A"
                        set "total=%%~B"
                        set "passed=%%~C"
                        set "failed=%%~D"
                        set "skippedRaw=%%~E"
                    )

                    set "skippedRaw=!skippedRaw:\"=!"
                    for /f "delims=>" %%s in ("!skippedRaw!") do set "skipped=%%s"

                    (
                        echo TOTAL_TESTS=!total!
                        echo PASSED_TESTS=!passed!
                        echo FAILED_TESTS=!failed!
                        echo SKIPPED_TESTS=!skipped!
                    ) > build.properties
                """
            }
        }

        stage('Load Results') {
            steps {
                script {
                    def props = readProperties file: 'build.properties'
                    env.TOTAL_TESTS = props.TOTAL_TESTS
                    env.PASSED_TESTS = props.PASSED_TESTS
                    env.FAILED_TESTS = props.FAILED_TESTS
                    env.SKIPPED_TESTS = props.SKIPPED_TESTS
                }
            }
        }

        stage('Send Email') {
            steps {
                emailext (
                    subject: "[LinhNguyen - Jenkin Automation Report] ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                    body: '''<!DOCTYPE html>
<html>
<head><style>body { font-family: Arial } td, th { padding: 5px; }</style></head>
<body>
<p>Hi Team,</p>
<p>The automated test execution has been completed. Summary:</p>
<table border="1">
<tr><th>Project</th><td>${JOB_NAME}</td></tr>
<tr><th>Build Number</th><td>${BUILD_NUMBER}</td></tr>
<tr><th>Total</th><td>${TOTAL_TESTS}</td></tr>
<tr><th>Passed</th><td style="color:green">${PASSED_TESTS}</td></tr>
<tr><th>Failed</th><td style="color:red">${FAILED_TESTS}</td></tr>
<tr><th>Skipped</th><td>${SKIPPED_TESTS}</td></tr>
</table>
<p>Report is attached.</p>
</body>
</html>''',
                    mimeType: 'text/html',
                    attachLog: true,
                    to: 'thuylinh1102001@gmail.com'
                )
            }
        }
    }
}
