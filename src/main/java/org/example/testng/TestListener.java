package org.example.testng;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.example.report.AllureReport;
import org.example.utils.SharedParameter;
import org.openqa.selenium.OutputType;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class TestListener implements ITestListener, IExecutionListener, IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

    @Override
    public void onTestStart(ITestResult result) {
        String originalTestName = result.getMethod().getDescription();
        int retryCount = getRetryCount(result);
        if (retryCount >= 1) {
            String uniqueTestName = originalTestName + "_Retry_" + retryCount;
            String historyId = originalTestName + "_attempt_" + retryCount;

            Allure.getLifecycle().updateTestCase(testResult -> {
                testResult.setName(uniqueTestName);
                testResult.setHistoryId(historyId);
            });
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        attachScreenshot("Screenshot on test success");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        attachScreenshot("Screenshot on test failure");
    }

    @Override
    public void onStart(ITestContext context) {
        SharedParameter.RETRY_MODE = Objects.requireNonNullElse(context.getCurrentXmlTest().getParameter("retryMode"), "immediately");
        SharedParameter.RETRY_COUNT = Integer.parseInt(context.getCurrentXmlTest().getParameter("retryCount"));
        AllureReport.setupAllure();
    }

    private void attachScreenshot(String attachmentName) {
        byte[] screenshot = Selenide.screenshot(OutputType.BYTES);
        if (screenshot != null) {
            Allure.addAttachment(attachmentName, "image/png", new ByteArrayInputStream(screenshot), ".png");
        }
    }

    private int getRetryCount(ITestResult result) {
        return result.getMethod().getCurrentInvocationCount();
    }

}
