package com.khired.documentapprovalworkflow.ActivitiDelegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.util.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApprovedDocumentTaskDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(ApprovedDocumentTaskDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        logger.info("Verification Completed Task executed for process instance: {}", processInstanceId);

        try {
            String apiUrl = (String) execution.getVariable("apiUrl");
            String docToken = (String) execution.getVariable("docToken");
            String companyId = (String) execution.getVariable("companyId");
            boolean docApprovalResult = (boolean) execution.getVariable("docApprovalResult");

            // Create a JSONObject for the request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("processId", processInstanceId);
            requestBody.put("docToken", docToken);
            requestBody.put("companyId", companyId);
            requestBody.put("docApprovalResult", docApprovalResult);


            RestTemplate restTemplate = new RestTemplate();
            // Send POST request
  //          String response = restTemplate.postForObject(apiUrl, requestBody, String.class);

            // Log the response
  //          logger.info("API response: {}", response);

        } catch (Exception e) {
            logger.error("Error processing task for processInstanceId: {}", processInstanceId, e);
            throw new RuntimeException("Error processing task", e); // Adjust exception handling as per your application's needs
        }
    }
}
