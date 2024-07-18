package com.khired.documentapprovalworkflow.ControllerTestCases;

import com.khired.documentapprovalworkflow.Controller.InProgressDocumentController;
import com.khired.documentapprovalworkflow.Service.IDocumentStorageService;
import com.khired.documentapprovalworkflow.Service.IInProgressDocumentService;
import com.khired.documentapprovalworkflow.Model.DocumentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InProgressDocumentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IDocumentStorageService documentStorageService;

    @Mock
    private IInProgressDocumentService inProgressDocumentService;

    @InjectMocks
    private InProgressDocumentController inProgressDocumentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(inProgressDocumentController).build();
    }

    @Test
    public void testGetDocumentsByCompanyId() throws Exception {
        DocumentStorage document1 = mock(DocumentStorage.class);
        DocumentStorage document2 = mock(DocumentStorage.class);
        when(document1.getProcessInstanceOfDoc()).thenReturn("process1");
        when(document2.getProcessInstanceOfDoc()).thenReturn("process2");

        List<DocumentStorage> mockDocuments = List.of(document1, document2);
        when(documentStorageService.findByCompanyId(anyString())).thenReturn(mockDocuments);

        mockMvc.perform(get("/api/operators/in_progress/{companyId}", "1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(documentStorageService, times(1)).findByCompanyId("1234");
    }

    @Test
    public void testGetProcessDetailsByProcessId() throws Exception {
        Map<String, Object> mockProcessDetails = new HashMap<>();
        mockProcessDetails.put("key", "value");
        when(inProgressDocumentService.getProcessDetails(anyString())).thenReturn(mockProcessDetails);

        mockMvc.perform(get("/api/operators/process/{processId}", "process123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("value"));

        verify(inProgressDocumentService, times(1)).getProcessDetails("process123");
    }

    @Test
    public void testCompleteTask() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("processId", "process123");
        requestBody.put("taskName", "task1");
        requestBody.put("operatorId", "operator123");
        requestBody.put("docApprovalResult", "true");

        when(inProgressDocumentService.completeTask(anyString(), anyString(), anyBoolean(), anyString()))
                .thenReturn("Task completed successfully");

        mockMvc.perform(post("/api/operators/completeTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"processId\": \"process123\", \"taskName\": \"task1\", \"operatorId\": \"operator123\", \"docApprovalResult\": \"true\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task completed successfully"));

        verify(inProgressDocumentService, times(1)).completeTask("process123", "task1", true, "operator123");
    }

}
