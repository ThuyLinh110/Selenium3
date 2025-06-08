# Automated testing web application by using Java, Selenide

This is an example of using Selenide test framework.

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

- In case you want to change the configuration of browser, you can change it in "/src/main/resources/configuration/[browser].json"
- Commonly used configurations

| Config          |  Type   |                                                                          Description | 
|-----------------|:-------:|-------------------------------------------------------------------------------------:|
| browser         | String  |                                                                      Running browser |
| timeout         |  long   |                                           Default timeout for waiting condition (ms) |
| pageLoadTimeout |  long   |                                      Maximum time for page is loaded completely (ms) |
| startMaximized  | boolean |                                                   Browser start with maximize or not |


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
