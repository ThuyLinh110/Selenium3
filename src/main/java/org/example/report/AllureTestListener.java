package org.example.report;

import io.qameta.allure.Allure;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import lombok.extern.slf4j.Slf4j;
import org.example.driver.DriverUtils;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;

@Slf4j
public class AllureTestListener implements TestLifecycleListener {

    @Override
    public void beforeTestStop(TestResult result) {
        if (result.getStatus().equals(Status.BROKEN) || result.getStatus().equals(Status.FAILED)) {
            log.info("Test case \"{}\" has been {}. Take a screenshot", result.getFullName(),
                    result.getStatus().value());
            try {
                ByteArrayInputStream input = new ByteArrayInputStream(DriverUtils.getCurrentDriver().screenshot(OutputType.BYTES));
                Allure.addAttachment("ScreenShot - Failed at" + result.getFullName(), input);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }
}
