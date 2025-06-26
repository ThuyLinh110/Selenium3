# Automated testing web application by using Java, Selenide

This is an example of using Selenide test framework.

## Task-progress

- [x] **Build selenide framework**
- [ ] **Report**
    - [x] Allure Report
    - [ ] Report Portal
- [ ] **Retry failed testcases**
    - [x] Retry immediately after the testcase failed
    - [ ] Retry failed testcases after all testcase done
- [x] Parallel execution
- [x] Cross browser testing
- [ ] Selenium Grid
- [ ] Implement testcase
    - [x] Agoda - TC1
    - [x] Agoda - TC2
    - [ ] Agoda - TC3
    - [ ] Vietjet - TC1
    - [ ] Vietjet - TC2
- [x] CI - Schedule test and send the notification result email

## Pre-requites

- Install Java 21 or above
- Install Maven

## Installation

- CD to the project folder
- Open CMD/terminal then type

```cmd
mvn clean install
```

## Execute

- You can run test suite by this command line.

```cmd
mvn clean test -DsuiteXmlFile=\src\test\resources\suites\TestSuites.xml
```

- In case you want to change the configuration of browser, you can change it in "
  /src/main/resources/configuration/[browser].json"

- In case you want to change parameters when running script, you can change them on the TestSuite.xml file or by command
  line

```cmd
mvn clean test -DsuiteXmlFile=src/test/resources/suites/TestSuites.xml -Dbrowser=chrome -Dlanguage=en -Denvironment=agoda
```

| Parameter       | Description                                                                                                                                                   | 
|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| browser         | Running browser <br/>e.g. `chrome`, `edge`                                                                                                                    |
| environment     | Running environment<br/>- `agoda`: Running script on Agoda website<br/>- `vietjet`: Running script on Vietjet website                                         |
| language        | Running language<br/>- `vi`: vietnamese<br/>- `en`: english                                                                                                   |
| retryMode       | Retry failed testcase<br/>- `immediately`: Retry immediately after the testcase failed<br/>- `post-suite`: Retry failed testcase after all testcase completed |
| retryCount      | Number of retries                                                                                                                                             |

## Report

- Allure report is using in this framework

  ### Installation
    - You can refer to this link to install allure: [link](https://docs.qameta.io/allure/#_installing_a_commandline)
  ### Report generation
    - After running the test, the report is generated on the /allure-result directory
    - Use this command from project folder to generate report

```cmd
allure serve
```
