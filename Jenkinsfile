pipeline {
    agent any

    parameters {
        choice(name: 'TEST_SUITE', choices: [
            '.\\src\\test\\resources\\suites\\TestSuites.xml',
            '.\\src\\test\\resources\\suites\\TestSuites_Vietjet.xml'
        ], description: 'Select the test suite')
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
                        sh "mvn clean test -DsuiteXmlFile=${testSuite}"
                    } else {
                        bat "mvn clean test -DsuiteXmlFile=${testSuite}"
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
                        echo "⚠️ Allure report generation failed: ${e.message}"
                    }
                } else {
                    echo "⚠️ No Allure results found."
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

//                     writeFile file: 'build.properties', text:
//                         "TOTAL_TESTS=${total}\n" +
//                         "PASSED_TESTS=${passed}\n" +
//                         "FAILED_TESTS=${failed}\n" +
//                         "SKIPPED_TESTS=${skipped}\n"
//
//                     echo "✅ Wrote test summary to build.properties"
//
//                     if (isUnix()) {
//                         sh 'cat build.properties'
//                     } else {
//                         bat 'type build.properties'
//                     }

                } else {
                    echo "⚠️ testng-results.xml not found."
                }

//                 if (fileExists('build.properties')) {
//                     def props = readProperties file: 'build.properties'
//                     total = props['TOTAL_TESTS'] ?: 'N/A'
//                     passed = props['PASSED_TESTS'] ?: 'N/A'
//                     failed = props['FAILED_TESTS'] ?: 'N/A'
//                     skipped = props['SKIPPED_TESTS'] ?: 'N/A'
//                 } else {
//                     echo "⚠️ build.properties not found — using fallback values for email."
//                 }

                emailext(
                    subject: "[LinhNguyen - Jenkins Automation Report] ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                    body: """<!DOCTYPE html>
                    <html>
                    <head><style>body { font-family: Arial } td, th { padding: 5px; }</style></head>
                    <body>
                    <p>Hi Team,</p>
                    <p>The automated test execution has been completed. Summary:</p>
                    <table border="1">
                    <tr><th>Project</th><td>${env.JOB_NAME}</td></tr>
                    <tr><th>Build Number</th><td>${env.BUILD_NUMBER}</td></tr>
                    <tr><th>Total</th><td>${total}</td></tr>
                    <tr><th>Passed</th><td style="color:green">${passed}</td></tr>
                    <tr><th>Failed</th><td style="color:red">${failed}</td></tr>
                    <tr><th>Skipped</th><td>${skipped}</td></tr>
                    </table>
                    <p>Report is attached (if available).</p>
                    </body>
                    </html>""",
                    mimeType: 'text/html',
                    attachLog: true,
                    attachmentsPattern: 'allure-report/index.html',
                    to: 'thuylinh1102001@gmail.com'
                )

            }

        }

    }
}
