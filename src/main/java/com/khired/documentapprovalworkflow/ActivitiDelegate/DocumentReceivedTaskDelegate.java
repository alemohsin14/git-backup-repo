package com.khired.documentapprovalworkflow.ActivitiDelegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DocumentReceivedTaskDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(DocumentReceivedTaskDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        logger.info("Document Received Task executed for process instance: {}", processInstanceId);
        logger.info("Service Task No 1");
        System.out.println("Service Task No 1");
        // Add your logic here to send notification to managers
    }
}
