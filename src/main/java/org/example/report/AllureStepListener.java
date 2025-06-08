package org.example.report;

import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllureStepListener implements StepLifecycleListener {

    @Override
    public void beforeStepStart(StepResult result) {
        log.info("[Step]: {}", result.getName());
    }
}
