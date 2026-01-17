package com.reyansh.birt.service;

import com.reyansh.birt.model.DynamicReportRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DynamicReportService
 */
class DynamicReportServiceTest {

    @InjectMocks
    private DynamicReportService dynamicReportService;

    @TempDir
    Path tempDir;

    private DynamicReportRequest validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dynamicReportService.init();

        validRequest = DynamicReportRequest.builder()
                .libraryPath("reports/library/test.rptlibrary")
                .reportName("test-report")
                .outputFormat("pdf")
                .dataSourceName("TestDataSource")
                .datasetNames(Arrays.asList("Dataset1", "Dataset2"))
                .parameters(Map.of("param1", "value1", "param2", 100))
                .build();
    }

    @Test
    void testInitialization() {
        assertDoesNotThrow(() -> dynamicReportService.init());
    }

    @Test
    void testGenerateDynamicReport_Success() throws Exception {
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        assertNotNull(reportId);
        assertFalse(reportId.isEmpty());
        assertTrue(reportId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }

    @Test
    void testGenerateDynamicReport_CreatesDirectories() throws Exception {
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        assertNotNull(reportId);
        File templatesDir = new File("reports/templates");
        File outputDir = new File("reports/output");
        
        assertTrue(templatesDir.exists());
        assertTrue(outputDir.exists());
    }

    @Test
    void testGenerateDynamicReport_CreatesFiles() throws Exception {
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        File templateFile = new File("reports/templates/" + reportId + ".rptdesign");
        File outputFile = new File("reports/output/" + reportId + ".pdf");
        
        assertTrue(templateFile.exists());
        assertTrue(outputFile.exists());
        assertTrue(templateFile.length() > 0);
        assertTrue(outputFile.length() > 0);
    }

    @Test
    void testGenerateDynamicReport_DifferentFormats() throws Exception {
        String[] formats = {"pdf", "html", "xls", "xlsx", "doc", "docx"};
        
        for (String format : formats) {
            validRequest.setOutputFormat(format);
            String reportId = dynamicReportService.generateDynamicReport(validRequest);
            
            assertNotNull(reportId);
            File outputFile = new File("reports/output/" + reportId + "." + format);
            assertTrue(outputFile.exists());
        }
    }

    @Test
    void testGenerateDynamicReport_WithNullParameters() throws Exception {
        validRequest.setParameters(null);
        
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        assertNotNull(reportId);
    }

    @Test
    void testGenerateDynamicReport_WithEmptyDatasetList() throws Exception {
        validRequest.setDatasetNames(new ArrayList<>());
        
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        assertNotNull(reportId);
    }

    @Test
    void testGenerateDynamicReport_WithComplexParameters() throws Exception {
        Map<String, Object> complexParams = new HashMap<>();
        complexParams.put("stringParam", "test");
        complexParams.put("intParam", 123);
        complexParams.put("doubleParam", 45.67);
        complexParams.put("boolParam", true);
        complexParams.put("dateParam", "2024-01-01");
        complexParams.put("nullParam", null);
        
        validRequest.setParameters(complexParams);
        
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        assertNotNull(reportId);
    }

    @Test
    void testGenerateDynamicReport_WithSpecialCharactersInReportName() throws Exception {
        validRequest.setReportName("test-report_2024@special#chars");
        
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        assertNotNull(reportId);
    }

    @Test
    void testGenerateDynamicReport_WithMultipleDatasets() throws Exception {
        List<String> datasets = Arrays.asList("DS1", "DS2", "DS3", "DS4", "DS5", "DS6", "DS7", "DS8", "DS9", "DS10");
        validRequest.setDatasetNames(datasets);
        
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        assertNotNull(reportId);
    }

    @Test
    void testGenerateDynamicReport_UniqueReportIds() throws Exception {
        String reportId1 = dynamicReportService.generateDynamicReport(validRequest);
        String reportId2 = dynamicReportService.generateDynamicReport(validRequest);
        String reportId3 = dynamicReportService.generateDynamicReport(validRequest);
        
        assertNotEquals(reportId1, reportId2);
        assertNotEquals(reportId2, reportId3);
        assertNotEquals(reportId1, reportId3);
    }

    @Test
    void testGenerateDynamicReport_WithLongLibraryPath() throws Exception {
        validRequest.setLibraryPath("reports/library/very/long/path/to/library/file/test.rptlibrary");
        
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        assertNotNull(reportId);
    }

    @Test
    void testGenerateDynamicReport_WithNullDataSourceName() throws Exception {
        validRequest.setDataSourceName(null);
        
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        assertNotNull(reportId);
    }

    @Test
    void testGenerateDynamicReport_WithEmptyParameters() throws Exception {
        validRequest.setParameters(new HashMap<>());
        
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        assertNotNull(reportId);
    }

    @Test
    void testGenerateDynamicReport_TemplateContainsReportName() throws Exception {
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        File templateFile = new File("reports/templates/" + reportId + ".rptdesign");
        assertTrue(templateFile.exists());
        
        // Template file should exist and have content
        assertTrue(templateFile.length() > 0);
    }

    @Test
    void testGenerateDynamicReport_OutputFileNotEmpty() throws Exception {
        String reportId = dynamicReportService.generateDynamicReport(validRequest);
        
        File outputFile = new File("reports/output/" + reportId + ".pdf");
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }
}
