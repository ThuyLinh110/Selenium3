pipeline {
    agent any

    parameters {
        choice(name: 'TEST_SUITE', choices: [
            '.\\src\\test\\resources\\suites\\TestSuites_Agoda.xml',
            '.\\src\\test\\resources\\suites\\TestSuites_Vietjet.xml',
            '.\\src\\test\\resources\\suites\\TestSuites_LeapFrog.xml',
            '.\\src\\test\\resources\\suites\\TestSuites_AllTestCases.xml',
        ], description: 'Select the test suite')

       choice(name: 'BROWSER', choices: ['chrome', 'edge'], description: 'Select the browser')
       choice(name: 'LANGUAGE', choices: ['vi', 'en'], description: 'Select the language')
       choice(name: 'RETRY_MODE', choices: ['immediately'], description: 'Select the retry mode')
       choice(name: 'RETRY_COUNT', choices: ['0', '1', '2'], description: 'Select the retry count')

    }

    environment {
        TOTAL_TESTS = ''
        PASSED_TESTS = ''
        FAILED_TESTS = ''
        SKIPPED_TESTS = ''
    }

    stages {
        stage('Build and Test with Maven') {
            steps {
                script {
                    def testSuite = isUnix()
                        ? params.TEST_SUITE.replace("\\", "/")
                        : params.TEST_SUITE

                    if (isUnix()) {
                        sh '''
                            rm -rf allure-results
                            rm -rf target/surefire-reports
                        '''
                    } else {
                        bat '''
                            if exist allure-results rmdir /s /q allure-results
                            if exist target\\surefire-reports rmdir /s /q target\\surefire-reports
                        '''
                    }

                    if (isUnix()) {
                        sh """
                            mvn clean test \\
                            -DsuiteXmlFile=${testSuite} \\
                            -Dbrowser=${params.BROWSER} \\
                            -Dlanguage=${params.LANGUAGE} \\
                            -DretryMode=${params.RETRY_MODE} \\
                            -DretryCount=${params.RETRY_COUNT}
                        """
                    } else {
                        bat """
                            mvn clean test ^
                            -DsuiteXmlFile=${testSuite} ^
                            -Dbrowser=${params.BROWSER} ^
                            -Dlanguage=${params.LANGUAGE} ^
                            -DretryMode=${params.RETRY_MODE} ^
                            -DretryCount=${params.RETRY_COUNT}
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                def total = 'N/A'
                def passed = 'N/A'
                def failed = 'N/A'
                def skipped = 'N/A'
                // Define the recursive logAttributes function inside script block
                def logAttributes
                logAttributes = { node ->
                    node.attributes().each { key, value ->
                        println "${key} = ${value}"
                    }
                    node.children().each { child ->
                        logAttributes(child)
                    }
                }

                // Generate Allure report
                if (fileExists('allure-results')) {
                    try {
                        def allureCommand = 'allure generate allure-results --clean --single-file -o allure-report'
                        if (isUnix()) {
                            sh allureCommand
                        } else {
                            bat allureCommand
                        }
                    } catch (Exception e) {
                        echo "Allure report generation failed: ${e.message}"
                    }
                } else {
                    echo "No Allure results found."
                }

                // Parse testng-results.xml
                def reportPath = isUnix()
                    ? 'target/surefire-reports/testng-results.xml'
                    : 'target\\surefire-reports\\testng-results.xml'

                if (fileExists(reportPath)) {
                    def content = readFile(reportPath)
                    def xml = new XmlSlurper().parseText(content)

                    logAttributes(xml)

                    total = xml.attributes().get('total').toString()
                    passed = xml.attributes().get('passed').toString()
                    failed = xml.attributes().get('failed').toString()
                    skipped = xml.attributes().get('skipped').toString()

                    echo "Parsed values → total=${total}, passed=${passed}, failed=${failed}, skipped=${skipped}"

                } else {
                    echo "⚠️ testng-results.xml not found."
                }

                emailext(
                    subject: "[LinhNguyen - Jenkins Automation Report] ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                    body: """<!DOCTYPE html>
                            <html>
                            <head><style>body { font-family: Arial } td, th { padding: 5px; }</style></head>
                            <body>
                            <p>Hi Team,</p>
                            <p>The automated test execution has been completed. Below is the summary report:</p>
                            <table border="1">
                            <tr><th>Project</th><td>${env.JOB_NAME}</td></tr>
                            <tr><th>Build Number</th><td>${env.BUILD_NUMBER}</td></tr>
                            <tr><th>Total</th><td>${total}</td></tr>
                            <tr><th>Passed</th><td style="color:green">${passed}</td></tr>
                            <tr><th>Failed</th><td style="color:red">${failed}</td></tr>
                            <tr><th>Skipped</th><td>${skipped}</td></tr>
                            </table>
                            <p>The report is attached as .html file. Please download and open it in your browser to see detail.</p>
                            <p>Best regards,<br/>Jenkins CI</p>
                            </body>
                            </html>""",
                    mimeType: 'text/html',
                    attachLog: false,
                    attachmentsPattern: 'allure-report/index.html',
                    to: 'thuylinh1102001@gmail.com'
                )
            }
        }
    }
}
