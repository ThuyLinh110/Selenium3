package org.example.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.example.driver.DriverUtils;
import org.openqa.selenium.OutputType;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.collections.Maps;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class SoftAssertion extends Assertion {

    private static final String DEFAULT_SOFT_ASSERT_MESSAGE = "The assertion is failed! ";
    private Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();

    public void assertAll() {
        assertAll(null);
    }

    public void assertAll(String message) {
        if (!m_errors.isEmpty()) {
            Allure.step(message + " - Total check point failed: " + m_errors.size(), Status.FAILED);
            Map<AssertionError, IAssert<?>> tempErrors = m_errors;
            m_errors = Maps.newLinkedHashMap();
            throw new AssertionError(tempErrors);
        } else {
            Allure.step(message, Status.PASSED);
        }
    }

    @Override
    protected void doAssert(IAssert<?> a) {
        onBeforeAssert(a);
        try {
            a.doAssert();
            onAssertSuccess(a);
            Allure.step(a.getMessage(), Status.PASSED);
        } catch (AssertionError ex) {
            onAssertFailure(a, ex);
            m_errors.put(ex, a);
            Allure.step(DEFAULT_SOFT_ASSERT_MESSAGE + getErrorDetails(ex), Status.FAILED);
            if (DriverUtils.isDriverAlive()) {
                ByteArrayInputStream input = new ByteArrayInputStream(DriverUtils.getCurrentDriver().screenshot(OutputType.BYTES));
                Allure.addAttachment("ScreenShot - " + "Failed at " + a.getMessage(), input);
            }
        } finally {
            onAfterAssert(a);
        }
    }
}
