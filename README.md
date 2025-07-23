# Automated testing web application by using Java, Selenide

This is an example of using Selenide test framework.

## Task-progress

### Output

- [x] **Build selenide framework**
- [ ] **Report**
    - [x] Allure Report
    - [ ] Report Portal
- [ ] **Retry failed testcases**
    - [x] Retry immediately after the testcase failed
    - [ ] Retry failed testcases after all testcase done
- [x] Parallel execution
- [x] Cross browser testing
- [x] Selenium Grid
- [ ] Implement testcase
    - [x] Agoda - TC1
    - [x] Agoda - TC2
    - [x] Agoda - TC3
    - [x] Vietjet - TC1
    - [ ] Vietjet - TC2
- [x] CI - Schedule test and send the notification result email

### Use case

- [x] Content testing
- [x] Multiple languages testing
- [x] Group tests by purposes: regression, smoke/sanity test
- [x] Source control practice: branch => create PR
- [x] Switch test environment: dev, stg (dev: agoda.com, stg: vj.com)
- [x] Wrap custom controls
- [ ] Data driven testing: test data is in excel file
- [ ] Working with Shadow DOM
- [ ] Compare with another FW e.g. Playwright

## Pre-requites

### 1. Install Java 21 or above

- Download from [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html)
- Verify installation:
  ```cmd
  java -version

### 2. Install Maven

- Download from [Apache Maven](https://maven.apache.org/download.cgi)
- Verify installation:
  ```cmd
  mvn -version

### 3. Install Selenium Gird (Optional)

- Download the Selenium Server (Grid) from [Download Selenium](https://www.selenium.dev/downloads/)
- Place it in a known directory, e.g., C:\selenium\selenium-server-4.34.0.jar
- Start the hub:
  ```cmd
  java -jar selenium-server-4.34.0.jar hub

- In another terminal, start the node:
  ```cmd
  java -jar selenium-server-4.34.0.jar node --hub http://localhost:4444 --selenium-manager true

## Installation

- CD to the project folder
- Open CMD/terminal then type

```cmd
mvn clean install
```

## Execute

### Option 1. Run via Command Line (Maven)

```cmd
mvn clean test 
  -DsuiteXmlFiles=./src/test/resources/suites/TestSuites.xml 
  -Dbrowser=chrome
  -Dtimeout=60000
  -DpageLoadStrategy=eager
  -Dremote=http://localhost:4444 # if running Selenium Grid
  -Dgroups=regression
  -Dparallel=tests
  -Denvironment=agoda
  -Dlanguage=en
  -DthreadCount=2
  -DretryCount=1
  -DretryMode=immediately
```

### Option 2. Manual execution via IDE

- Right-click on TestSuites.xml or a test class → Run

- Configuration values are read from:

    - /src/main/resources/configuration/{browser}.json

    - TestSuites.xml (for TestNG parameters)

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
