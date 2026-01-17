package com.reyansh.birt.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DynamicReportRequest model
 */
class DynamicReportRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDynamicReportRequest() {
        DynamicReportRequest request = DynamicReportRequest.builder()
                .libraryPath("reports/library/test.rptlibrary")
                .reportName("test-report")
                .outputFormat("pdf")
                .dataSourceName("TestDS")
                .datasetNames(Arrays.asList("Dataset1", "Dataset2"))
                .parameters(Map.of("param1", "value1"))
                .build();

        Set<ConstraintViolation<DynamicReportRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testDynamicReportRequest_MissingLibraryPath() {
        DynamicReportRequest request = DynamicReportRequest.builder()
                .reportName("test-report")
                .outputFormat("pdf")
                .datasetNames(Arrays.asList("Dataset1"))
                .build();

        Set<ConstraintViolation<DynamicReportRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("libraryPath")));
    }

    @Test
    void testDynamicReportRequest_MissingReportName() {
        DynamicReportRequest request = DynamicReportRequest.builder()
                .libraryPath("reports/library/test.rptlibrary")
                .outputFormat("pdf")
                .datasetNames(Arrays.asList("Dataset1"))
                .build();

        Set<ConstraintViolation<DynamicReportRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("reportName")));
    }

    @Test
    void testDynamicReportRequest_EmptyDatasetNames() {
        DynamicReportRequest request = DynamicReportRequest.builder()
                .libraryPath("reports/library/test.rptlibrary")
                .reportName("test-report")
                .outputFormat("pdf")
                .datasetNames(Collections.emptyList())
                .build();

        Set<ConstraintViolation<DynamicReportRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("datasetNames")));
    }

    @Test
    void testDynamicReportRequest_Builder() {
        List<String> datasets = Arrays.asList("DS1", "DS2", "DS3");
        Map<String, Object> params = Map.of("param1", "value1", "param2", 100);

        DynamicReportRequest request = DynamicReportRequest.builder()
                .libraryPath("reports/library/test.rptlibrary")
                .reportName("test-report")
                .outputFormat("pdf")
                .dataSourceName("TestDS")
                .datasetNames(datasets)
                .parameters(params)
                .build();

        assertEquals("reports/library/test.rptlibrary", request.getLibraryPath());
        assertEquals("test-report", request.getReportName());
        assertEquals("pdf", request.getOutputFormat());
        assertEquals("TestDS", request.getDataSourceName());
        assertEquals(datasets, request.getDatasetNames());
        assertEquals(params, request.getParameters());
    }

    @Test
    void testDynamicReportRequest_NoArgsConstructor() {
        DynamicReportRequest request = new DynamicReportRequest();
        assertNotNull(request);
    }

    @Test
    void testDynamicReportRequest_AllArgsConstructor() {
        List<String> datasets = Arrays.asList("DS1", "DS2");
        Map<String, Object> params = Map.of("param1", "value1");

        DynamicReportRequest request = new DynamicReportRequest(
                "reports/library/test.rptlibrary",
                "test-report",
                "pdf",
                "TestDS",
                datasets,
                params,
                null
        );

        assertEquals("reports/library/test.rptlibrary", request.getLibraryPath());
        assertEquals("test-report", request.getReportName());
        assertEquals("pdf", request.getOutputFormat());
        assertEquals("TestDS", request.getDataSourceName());
        assertEquals(datasets, request.getDatasetNames());
        assertEquals(params, request.getParameters());
    }

    @Test
    void testDynamicReportRequest_SettersAndGetters() {
        DynamicReportRequest request = new DynamicReportRequest();
        request.setLibraryPath("test/path.rptlibrary");
        request.setReportName("report");
        request.setOutputFormat("html");
        request.setDataSourceName("DS");
        request.setDatasetNames(Arrays.asList("Dataset1"));
        request.setParameters(Map.of("key", "value"));

        assertEquals("test/path.rptlibrary", request.getLibraryPath());
        assertEquals("report", request.getReportName());
        assertEquals("html", request.getOutputFormat());
        assertEquals("DS", request.getDataSourceName());
        assertEquals(1, request.getDatasetNames().size());
        assertEquals(1, request.getParameters().size());
    }

    @Test
    void testDynamicReportRequest_WithMultipleDatasets() {
        List<String> datasets = Arrays.asList("DS1", "DS2", "DS3", "DS4", "DS5");

        DynamicReportRequest request = DynamicReportRequest.builder()
                .libraryPath("reports/library/test.rptlibrary")
                .reportName("test-report")
                .outputFormat("pdf")
                .datasetNames(datasets)
                .build();

        Set<ConstraintViolation<DynamicReportRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
        assertEquals(5, request.getDatasetNames().size());
    }

    @Test
    void testDynamicReportRequest_WithComplexParameters() {
        Map<String, Object> complexParams = new HashMap<>();
        complexParams.put("stringParam", "test");
        complexParams.put("intParam", 123);
        complexParams.put("doubleParam", 45.67);
        complexParams.put("boolParam", true);
        complexParams.put("dateParam", "2024-01-01");

        DynamicReportRequest request = DynamicReportRequest.builder()
                .libraryPath("reports/library/test.rptlibrary")
                .reportName("test-report")
                .outputFormat("pdf")
                .datasetNames(Arrays.asList("Dataset1"))
                .parameters(complexParams)
                .build();

        Set<ConstraintViolation<DynamicReportRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
        assertEquals(5, request.getParameters().size());
    }

    @Test
    void testDynamicReportRequest_WithNullDataSourceName() {
        DynamicReportRequest request = DynamicReportRequest.builder()
                .libraryPath("reports/library/test.rptlibrary")
                .reportName("test-report")
                .outputFormat("pdf")
                .dataSourceName(null)
                .datasetNames(Arrays.asList("Dataset1"))
                .build();

        Set<ConstraintViolation<DynamicReportRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
        assertNull(request.getDataSourceName());
    }

    @Test
    void testDynamicReportRequest_WithNullParameters() {
        DynamicReportRequest request = DynamicReportRequest.builder()
                .libraryPath("reports/library/test.rptlibrary")
                .reportName("test-report")
                .outputFormat("pdf")
                .datasetNames(Arrays.asList("Dataset1"))
                .parameters(null)
                .build();

        Set<ConstraintViolation<DynamicReportRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
        assertNull(request.getParameters());
    }

    @Test
    void testDynamicReportRequest_EqualsAndHashCode() {
        DynamicReportRequest request1 = DynamicReportRequest.builder()
                .libraryPath("reports/library/test.rptlibrary")
                .reportName("test-report")
                .outputFormat("pdf")
                .datasetNames(Arrays.asList("Dataset1"))
                .build();

        DynamicReportRequest request2 = DynamicReportRequest.builder()
                .libraryPath("reports/library/test.rptlibrary")
                .reportName("test-report")
                .outputFormat("pdf")
                .datasetNames(Arrays.asList("Dataset1"))
                .build();

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testDynamicReportRequest_ToString() {
        DynamicReportRequest request = DynamicReportRequest.builder()
                .libraryPath("reports/library/test.rptlibrary")
                .reportName("test-report")
                .outputFormat("pdf")
                .datasetNames(Arrays.asList("Dataset1"))
                .build();

        String toString = request.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("test-report"));
        assertTrue(toString.contains("pdf"));
    }

    @Test
    void testDynamicReportRequest_AllOutputFormats() {
        String[] formats = {"pdf", "html", "xls", "xlsx", "doc", "docx"};

        for (String format : formats) {
            DynamicReportRequest request = DynamicReportRequest.builder()
                    .libraryPath("reports/library/test.rptlibrary")
                    .reportName("test-report")
                    .outputFormat(format)
                    .datasetNames(Arrays.asList("Dataset1"))
                    .build();

            Set<ConstraintViolation<DynamicReportRequest>> violations = validator.validate(request);
            assertTrue(violations.isEmpty(), "Format " + format + " should be valid");
        }
    }
}
