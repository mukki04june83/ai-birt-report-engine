package com.reyansh.birt.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ReportRequest model
 */
class ReportRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidReportRequest() {
        ReportRequest request = ReportRequest.builder()
                .reportName("test-report")
                .outputFormat("pdf")
                .parameters(Map.of("param1", "value1"))
                .locale("en_US")
                .build();

        Set<ConstraintViolation<ReportRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testReportRequest_MissingReportName() {
        ReportRequest request = ReportRequest.builder()
                .outputFormat("pdf")
                .build();

        Set<ConstraintViolation<ReportRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("reportName")));
    }

    @Test
    void testReportRequest_MissingOutputFormat() {
        ReportRequest request = ReportRequest.builder()
                .reportName("test-report")
                .build();

        Set<ConstraintViolation<ReportRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("outputFormat")));
    }

    @Test
    void testReportRequest_InvalidOutputFormat() {
        ReportRequest request = ReportRequest.builder()
                .reportName("test-report")
                .outputFormat("invalid")
                .build();

        Set<ConstraintViolation<ReportRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("outputFormat")));
    }

    @Test
    void testReportRequest_AllValidFormats() {
        String[] validFormats = {"pdf", "html", "xls", "xlsx", "doc", "docx", "ppt", "pptx", "xml"};
        
        for (String format : validFormats) {
            ReportRequest request = ReportRequest.builder()
                    .reportName("test-report")
                    .outputFormat(format)
                    .build();

            Set<ConstraintViolation<ReportRequest>> violations = validator.validate(request);
            assertTrue(violations.isEmpty(), "Format " + format + " should be valid");
        }
    }

    @Test
    void testReportRequest_Builder() {
        Map<String, Object> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", 100);

        ReportRequest request = ReportRequest.builder()
                .reportName("test-report")
                .outputFormat("pdf")
                .parameters(params)
                .outputFileName("custom-output")
                .locale("en_US")
                .pageRange("1-5")
                .build();

        assertEquals("test-report", request.getReportName());
        assertEquals("pdf", request.getOutputFormat());
        assertEquals(params, request.getParameters());
        assertEquals("custom-output", request.getOutputFileName());
        assertEquals("en_US", request.getLocale());
        assertEquals("1-5", request.getPageRange());
    }

    @Test
    void testReportRequest_NoArgsConstructor() {
        ReportRequest request = new ReportRequest();
        assertNotNull(request);
    }

    @Test
    void testReportRequest_AllArgsConstructor() {
        Map<String, Object> params = Map.of("param1", "value1");
        ReportRequest request = new ReportRequest(
                "test-report",
                "pdf",
                params,
                "output.pdf",
                "en_US",
                "1-10"
        );

        assertEquals("test-report", request.getReportName());
        assertEquals("pdf", request.getOutputFormat());
        assertEquals(params, request.getParameters());
        assertEquals("output.pdf", request.getOutputFileName());
        assertEquals("en_US", request.getLocale());
        assertEquals("1-10", request.getPageRange());
    }

    @Test
    void testReportRequest_SettersAndGetters() {
        ReportRequest request = new ReportRequest();
        request.setReportName("test-report");
        request.setOutputFormat("html");
        request.setParameters(Map.of("key", "value"));
        request.setOutputFileName("report.html");
        request.setLocale("fr_FR");
        request.setPageRange("all");

        assertEquals("test-report", request.getReportName());
        assertEquals("html", request.getOutputFormat());
        assertNotNull(request.getParameters());
        assertEquals("report.html", request.getOutputFileName());
        assertEquals("fr_FR", request.getLocale());
        assertEquals("all", request.getPageRange());
    }

    @Test
    void testReportRequest_WithNullParameters() {
        ReportRequest request = ReportRequest.builder()
                .reportName("test-report")
                .outputFormat("pdf")
                .parameters(null)
                .build();

        Set<ConstraintViolation<ReportRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
        assertNull(request.getParameters());
    }

    @Test
    void testReportRequest_WithEmptyParameters() {
        ReportRequest request = ReportRequest.builder()
                .reportName("test-report")
                .outputFormat("pdf")
                .parameters(new HashMap<>())
                .build();

        Set<ConstraintViolation<ReportRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
        assertTrue(request.getParameters().isEmpty());
    }

    @Test
    void testReportRequest_EqualsAndHashCode() {
        ReportRequest request1 = ReportRequest.builder()
                .reportName("test-report")
                .outputFormat("pdf")
                .build();

        ReportRequest request2 = ReportRequest.builder()
                .reportName("test-report")
                .outputFormat("pdf")
                .build();

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testReportRequest_ToString() {
        ReportRequest request = ReportRequest.builder()
                .reportName("test-report")
                .outputFormat("pdf")
                .build();

        String toString = request.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("test-report"));
        assertTrue(toString.contains("pdf"));
    }
}
