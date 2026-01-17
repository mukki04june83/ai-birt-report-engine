package com.reyansh.birt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reyansh.birt.model.DynamicReportRequest;
import com.reyansh.birt.model.ReportRequest;
import com.reyansh.birt.service.DynamicReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for ReportController
 */
@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DynamicReportService dynamicReportService;

    private DynamicReportRequest validDynamicRequest;
    private ReportRequest validReportRequest;

    @BeforeEach
    void setUp() {
        // Setup valid dynamic report request
        validDynamicRequest = DynamicReportRequest.builder()
                .libraryPath("reports/library/common.rptlibrary")
                .reportName("test-report")
                .outputFormat("pdf")
                .dataSourceName("TestDataSource")
                .datasetNames(Arrays.asList("Dataset1", "Dataset2"))
                .parameters(Map.of("param1", "value1", "param2", 100))
                .build();

        // Setup valid report request
        validReportRequest = ReportRequest.builder()
                .reportName("test-report")
                .outputFormat("pdf")
                .parameters(Map.of("param1", "value1"))
                .locale("en_US")
                .build();
    }

    @Test
    void testGenerateDynamicReport_Success() throws Exception {
        String reportId = UUID.randomUUID().toString();
        when(dynamicReportService.generateDynamicReport(any(DynamicReportRequest.class)))
                .thenReturn(reportId);

        mockMvc.perform(post("/api/reports/generate-dynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDynamicRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.reportId").value(reportId))
                .andExpect(jsonPath("$.outputFormat").value("pdf"))
                .andExpect(jsonPath("$.message").value("Report generated successfully"));

        verify(dynamicReportService, times(1)).generateDynamicReport(any(DynamicReportRequest.class));
    }

    @Test
    void testGenerateDynamicReport_MissingLibraryPath() throws Exception {
        validDynamicRequest.setLibraryPath(null);

        mockMvc.perform(post("/api/reports/generate-dynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDynamicRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        verify(dynamicReportService, never()).generateDynamicReport(any());
    }

    @Test
    void testGenerateDynamicReport_EmptyDatasetNames() throws Exception {
        validDynamicRequest.setDatasetNames(Collections.emptyList());

        mockMvc.perform(post("/api/reports/generate-dynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDynamicRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        verify(dynamicReportService, never()).generateDynamicReport(any());
    }

    @Test
    void testGenerateDynamicReport_ServiceException() throws Exception {
        when(dynamicReportService.generateDynamicReport(any(DynamicReportRequest.class)))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/api/reports/generate-dynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDynamicRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());

        verify(dynamicReportService, times(1)).generateDynamicReport(any(DynamicReportRequest.class));
    }

    @Test
    void testGenerateDynamicReport_InvalidOutputFormat() throws Exception {
        validDynamicRequest.setOutputFormat("");

        mockMvc.perform(post("/api/reports/generate-dynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDynamicRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        verify(dynamicReportService, never()).generateDynamicReport(any());
    }

    @Test
    void testGenerateDynamicReport_WithComplexParameters() throws Exception {
        Map<String, Object> complexParams = new HashMap<>();
        complexParams.put("stringParam", "test");
        complexParams.put("intParam", 123);
        complexParams.put("boolParam", true);
        complexParams.put("dateParam", "2024-01-01");
        
        validDynamicRequest.setParameters(complexParams);
        
        String reportId = UUID.randomUUID().toString();
        when(dynamicReportService.generateDynamicReport(any(DynamicReportRequest.class)))
                .thenReturn(reportId);

        mockMvc.perform(post("/api/reports/generate-dynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDynamicRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.reportId").value(reportId));

        verify(dynamicReportService, times(1)).generateDynamicReport(any(DynamicReportRequest.class));
    }

    @Test
    void testGenerateDynamicReport_NullReportName() throws Exception {
        validDynamicRequest.setReportName(null);

        mockMvc.perform(post("/api/reports/generate-dynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDynamicRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        verify(dynamicReportService, never()).generateDynamicReport(any());
    }

    @Test
    void testGenerateDynamicReport_MultipleDatasets() throws Exception {
        validDynamicRequest.setDatasetNames(Arrays.asList("DS1", "DS2", "DS3", "DS4", "DS5"));
        
        String reportId = UUID.randomUUID().toString();
        when(dynamicReportService.generateDynamicReport(any(DynamicReportRequest.class)))
                .thenReturn(reportId);

        mockMvc.perform(post("/api/reports/generate-dynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDynamicRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(dynamicReportService, times(1)).generateDynamicReport(any(DynamicReportRequest.class));
    }

    @Test
    void testGenerateDynamicReport_DifferentOutputFormats() throws Exception {
        String[] formats = {"pdf", "html", "xls", "xlsx"};
        
        for (String format : formats) {
            validDynamicRequest.setOutputFormat(format);
            String reportId = UUID.randomUUID().toString();
            when(dynamicReportService.generateDynamicReport(any(DynamicReportRequest.class)))
                    .thenReturn(reportId);

            mockMvc.perform(post("/api/reports/generate-dynamic")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validDynamicRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.outputFormat").value(format));
        }

        verify(dynamicReportService, times(formats.length)).generateDynamicReport(any(DynamicReportRequest.class));
    }

    @Test
    void testGenerateDynamicReport_EmptyParameters() throws Exception {
        validDynamicRequest.setParameters(new HashMap<>());
        
        String reportId = UUID.randomUUID().toString();
        when(dynamicReportService.generateDynamicReport(any(DynamicReportRequest.class)))
                .thenReturn(reportId);

        mockMvc.perform(post("/api/reports/generate-dynamic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDynamicRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(dynamicReportService, times(1)).generateDynamicReport(any(DynamicReportRequest.class));
    }

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/reports/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("BIRT Report Engine"));
    }

    @Test
    void testDeleteReport() throws Exception {
        String reportId = "test-report-123";
        
        mockMvc.perform(delete("/api/reports/" + reportId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Report deleted successfully"))
                .andExpect(jsonPath("$.reportId").value(reportId));
    }
}
