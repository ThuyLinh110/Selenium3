package org.example.testng;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.SharedParameter;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < SharedParameter.RETRY_COUNT) {
            retryCount++;
            log.warn("Test {} failed. Retrying {}/{}",
                    result.getName(), retryCount, SharedParameter.RETRY_COUNT);
            return true;
        }
        return false;
    }
}
