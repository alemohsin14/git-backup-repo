package com.khired.documentapprovalworkflow.Config;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ActivitiConfig {

    @Value("${bpmn.resource.path}")
    private String bpmnResource;

    private static final Logger logger = LoggerFactory.getLogger(ActivitiConfig.class);

    @Bean
    public ProcessEngineConfiguration processEngineConfiguration() {
        return ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:postgresql://localhost:5432/documentapprovalworkflow")
                .setJdbcUsername("postgres")
                .setJdbcPassword("123456")
                .setJdbcDriver("org.postgresql.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    }

    @Bean
    public ProcessEngine processEngine(ProcessEngineConfiguration processEngineConfiguration) {
        return processEngineConfiguration.buildProcessEngine();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public Deployment deployProcessDefinition(ProcessEngine processEngine) {
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // Deploy the BPMN file
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(bpmnResource)
                .deploy();

        logger.info("Deployment ID: {}", deployment.getId());
        logger.info("Deployment Name: {}", deployment.getName());

        return deployment;
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }
}
