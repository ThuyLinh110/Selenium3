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
        String retryCount = context.getCurrentXmlTest().getParameter("retryCount");
        SharedParameter.RETRY_COUNT = Objects.nonNull(retryCount) ? Integer.parseInt(retryCount) : 0;
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
