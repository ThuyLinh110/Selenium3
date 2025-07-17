pipeline {
    agent any

    parameters {
        choice(name: 'TEST_SUITE', choices: [
            '.\\src\\test\\resources\\suites\\TestSuites.xml',
            '.\\src\\test\\resources\\suites\\TestSuites_Vietjet.xml'
        ], description: 'Select test suite to run')

        // Use choice or string depending on your use case
        choice(name: 'EMAIL_TO', choices: [
            'thuylinh1102001@gmail.com'
        ], description: 'Select recipient for the test report email')
    }

    environment {
        MVN_EXIT_CODE = 0
        TOTAL_TESTS = 0
        PASSED_TESTS = 0
        FAILED_TESTS = 0
        SKIPPED_TESTS = 0
    }

    stages {
        stage('Clean & Test') {
            steps {
                echo "Running tests for suite: ${params.TEST_SUITE}"
                script {
                    MVN_EXIT_CODE = bat(script: "mvn clean test -DsuiteXmlFile=${params.TEST_SUITE} ", returnStatus: true)
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                bat 'allure generate allure-results --clean --single-file -o allure-report || true'
            }
        }

        stage('Parse testng-results.xml') {
            steps {
                script {
                    def xmlFile = 'target/surefire-reports/testng-results.xml'
                    if (!fileExists(xmlFile)) {
                        error "Missing testng-results.xml"
                    }

                    def xml = new XmlSlurper().parse(new File(xmlFile))
                    env.TOTAL_TESTS = xml.@total.toString()
                    env.PASSED_TESTS = xml.@passed.toString()
                    env.FAILED_TESTS = xml.@failed.toString()
                    env.SKIPPED_TESTS = xml.@skipped.toString()

                    writeFile file: 'build.properties', text: """
TOTAL_TESTS=${env.TOTAL_TESTS}
PASSED_TESTS=${env.PASSED_TESTS}
FAILED_TESTS=${env.FAILED_TESTS}
SKIPPED_TESTS=${env.SKIPPED_TESTS}
"""
                }
            }
        }

        stage('Fail Build on Test Failure') {
            steps {
                script {
                    if (MVN_EXIT_CODE != 0) {
                        error "Maven test failed with exit code ${MVN_EXIT_CODE}"
                    }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'build.properties', onlyIfSuccessful: false
            junit 'target/surefire-reports/*.xml'

            script {
                def htmlBody = """\
<!DOCTYPE html>
<html>
<head>
    <style>
        body {
            font-family: Arial, sans-serif;
            color: #333;
            padding: 20px;
        }
        table {
            border-collapse: collapse;
            width: 60%;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .highlight-pass {
            color: green;
            font-weight: bold;
        }
        .highlight-fail {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>

<p>Hi Team,</p>

<p>The automated test execution has been completed. Please find the summary below:</p>

<table>
    <tr>
        <th>Project</th>
        <td>${env.JOB_NAME}</td>
    </tr>
    <tr>
        <th>Build Number</th>
        <td>${env.BUILD_NUMBER}</td>
    </tr>
    <tr>
        <th>Total Tests</th>
        <td>${env.TOTAL_TESTS}</td>
    </tr>
    <tr>
        <th>Passed</th>
        <td class="highlight-pass">${env.PASSED_TESTS}</td>
    </tr>
    <tr>
        <th>Failed</th>
        <td class="highlight-fail">${env.FAILED_TESTS}</td>
    </tr>
    <tr>
        <th>Skipped</th>
        <td>${env.SKIPPED_TESTS}</td>
    </tr>
</table>

<p>The report (.html file) is attached to this email. Please download and open it to see details.</p>

<p>Regards,<br>Jenkin - Automation Report</p>

</body>
</html>
"""

                emailext(
                    subject: "[LinhNguyen - Jenkin Automation Report] ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                    body: htmlBody,
                    mimeType: 'text/html',
                    to: "${params.EMAIL_TO}",
                    attachmentsPattern: 'allure-report/index.html'
                )
            }
        }

        failure {
            echo 'Build failed.'
        }

        success {
            echo 'Build succeeded.'
        }
    }
}
