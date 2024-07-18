package com.khired.documentapprovalworkflow.ControllerTestCases;

import com.khired.documentapprovalworkflow.Service.ICompanyService;
import com.khired.documentapprovalworkflow.Service.IDocumentReceivedService;
import com.khired.documentapprovalworkflow.Controller.DocumentReceivedController;
import com.khired.documentapprovalworkflow.Model.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DocumentReceivedControllerTest {

    @Mock
    private IDocumentReceivedService documentReceivedService;

    @Mock
    private ICompanyService companyService;

    @InjectMocks
    private DocumentReceivedController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testStartProcessInstance_InvalidCompany() throws Exception {
        // Setup
        String companyId = "invalidCompanyId";
        when(companyService.getCompanyById(companyId)).thenReturn(Optional.empty());

        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", MediaType.TEXT_PLAIN_VALUE, "content".getBytes());

        // Perform and Verify
        mockMvc.perform(multipart("/api/verification/startProcess")
                        .file(file)
                        .param("companyId", companyId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Company with ID " + companyId + " does not exist."));
    }

    @Test
    public void testStartProcessInstance_EmptyFile() throws Exception {
        // Setup
        String companyId = "validCompanyId";
        when(companyService.getCompanyById(companyId)).thenReturn(Optional.of(new Company()));

        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", MediaType.TEXT_PLAIN_VALUE, "".getBytes());

        // Perform and Verify
        mockMvc.perform(multipart("/api/verification/startProcess")
                        .file(file)
                        .param("companyId", companyId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Uploaded file is empty."));
    }


    @Test
    public void testStartProcessInstance_Success() throws Exception {
        // Mock company existence check
        String companyId = "company1";
        Company company = new Company();
        company.setId(companyId);
        when(companyService.getCompanyById(companyId)).thenReturn(Optional.of(company));

        // Mock file upload
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        // Perform the POST request
        MvcResult result = mockMvc.perform(multipart("/api/verification/startProcess")
                        .file(file)
                        .param("companyId", companyId))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the response content
        String content = result.getResponse().getContentAsString();

        // Verify the response content
        assertTrue(content.contains("Process started with name: "));

        // Extract and verify the process instance name from the response content
        String processInstanceNameToken = content.replace("Process started with name: ", "");

        // Verify that the service method was called with the correct process instance name
        verify(documentReceivedService).startProcess(eq(processInstanceNameToken), anyString(), anyString());
    }


}
