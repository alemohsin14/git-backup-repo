package com.khired.documentapprovalworkflow.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class HistoricTaskInstanceDto {
    private String id;
    private String processInstanceId;
    private String processDefinitionId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date endTime;

    private Long durationInMillis;
    private String deleteReason;
    private String executionId;
    private String name;
    private String parentTaskId;
    private String description;
    private String owner;
    private String assignee;
    private String taskDefinitionKey;
    private String formKey;
    private Integer priority;
    private String dueDate;
    private String claimTime;
    private String category;
    private String tenantId;
    private String businessKey;
    private String createTime;

    // Getters and Setters (omitted for brevity)

    @Override
    public String toString() {
        return "HistoricTaskInstanceDTO{" +
                "id='" + id + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", durationInMillis=" + durationInMillis +
                ", deleteReason='" + deleteReason + '\'' +
                ", executionId='" + executionId + '\'' +
                ", name='" + name + '\'' +
                ", parentTaskId='" + parentTaskId + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", assignee='" + assignee + '\'' +
                ", taskDefinitionKey='" + taskDefinitionKey + '\'' +
                ", formKey='" + formKey + '\'' +
                ", priority=" + priority +
                ", dueDate='" + dueDate + '\'' +
                ", claimTime='" + claimTime + '\'' +
                ", category='" + category + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

}
