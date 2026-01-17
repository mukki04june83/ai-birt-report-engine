package com.reyansh.birt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ReportResponse model
 */
class ReportResponseTest {

    @Test
    void testSuccessResponse() {
        ReportResponse response = ReportResponse.success(
                "report-123",
                "reports/output/report-123.pdf",
                "pdf",
                1500L,
                "/api/reports/download/report-123"
        );

        assertTrue(response.isSuccess());
        assertEquals("Report generated successfully", response.getMessage());
        assertEquals("report-123", response.getReportId());
        assertEquals("reports/output/report-123.pdf", response.getOutputPath());
        assertEquals("pdf", response.getOutputFormat());
        assertEquals(1500L, response.getGenerationTimeMs());
        assertEquals("/api/reports/download/report-123", response.getDownloadUrl());
        assertNull(response.getError());
    }

    @Test
    void testErrorResponse() {
        ReportResponse response = ReportResponse.error("Test error message");

        assertFalse(response.isSuccess());
        assertEquals("Report generation failed", response.getMessage());
        assertEquals("Test error message", response.getError());
        assertNull(response.getReportId());
        assertNull(response.getOutputPath());
        assertNull(response.getOutputFormat());
        assertEquals(0, response.getGenerationTimeMs());
        assertNull(response.getDownloadUrl());
    }

    @Test
    void testBuilder() {
        ReportResponse response = ReportResponse.builder()
                .success(true)
                .message("Custom message")
                .reportId("custom-id")
                .outputPath("custom/path.pdf")
                .outputFormat("pdf")
                .generationTimeMs(2000L)
                .downloadUrl("/custom/download")
                .error(null)
                .build();

        assertTrue(response.isSuccess());
        assertEquals("Custom message", response.getMessage());
        assertEquals("custom-id", response.getReportId());
        assertEquals("custom/path.pdf", response.getOutputPath());
        assertEquals("pdf", response.getOutputFormat());
        assertEquals(2000L, response.getGenerationTimeMs());
        assertEquals("/custom/download", response.getDownloadUrl());
        assertNull(response.getError());
    }

    @Test
    void testNoArgsConstructor() {
        ReportResponse response = new ReportResponse();
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(0, response.getGenerationTimeMs());
    }

    @Test
    void testAllArgsConstructor() {
        ReportResponse response = new ReportResponse(
                true,
                "Test message",
                "id-123",
                "path/to/report.pdf",
                "pdf",
                1000L,
                "/download/123",
                null
        );

        assertTrue(response.isSuccess());
        assertEquals("Test message", response.getMessage());
        assertEquals("id-123", response.getReportId());
        assertEquals("path/to/report.pdf", response.getOutputPath());
        assertEquals("pdf", response.getOutputFormat());
        assertEquals(1000L, response.getGenerationTimeMs());
        assertEquals("/download/123", response.getDownloadUrl());
        assertNull(response.getError());
    }

    @Test
    void testSettersAndGetters() {
        ReportResponse response = new ReportResponse();
        
        response.setSuccess(true);
        response.setMessage("Test");
        response.setReportId("123");
        response.setOutputPath("test/path");
        response.setOutputFormat("html");
        response.setGenerationTimeMs(500L);
        response.setDownloadUrl("/download");
        response.setError("error");

        assertTrue(response.isSuccess());
        assertEquals("Test", response.getMessage());
        assertEquals("123", response.getReportId());
        assertEquals("test/path", response.getOutputPath());
        assertEquals("html", response.getOutputFormat());
        assertEquals(500L, response.getGenerationTimeMs());
        assertEquals("/download", response.getDownloadUrl());
        assertEquals("error", response.getError());
    }

    @Test
    void testSuccessResponse_DifferentFormats() {
        String[] formats = {"pdf", "html", "xls", "xlsx", "doc", "docx"};

        for (String format : formats) {
            ReportResponse response = ReportResponse.success(
                    "report-id",
                    "output/path." + format,
                    format,
                    1000L,
                    "/download"
            );

            assertTrue(response.isSuccess());
            assertEquals(format, response.getOutputFormat());
            assertTrue(response.getOutputPath().endsWith("." + format));
        }
    }

    @Test
    void testSuccessResponse_LongGenerationTime() {
        ReportResponse response = ReportResponse.success(
                "report-123",
                "output/path.pdf",
                "pdf",
                999999L,
                "/download"
        );

        assertEquals(999999L, response.getGenerationTimeMs());
    }

    @Test
    void testSuccessResponse_ZeroGenerationTime() {
        ReportResponse response = ReportResponse.success(
                "report-123",
                "output/path.pdf",
                "pdf",
                0L,
                "/download"
        );

        assertEquals(0L, response.getGenerationTimeMs());
    }

    @Test
    void testErrorResponse_NullMessage() {
        ReportResponse response = ReportResponse.error(null);

        assertFalse(response.isSuccess());
        assertNull(response.getError());
    }

    @Test
    void testErrorResponse_EmptyMessage() {
        ReportResponse response = ReportResponse.error("");

        assertFalse(response.isSuccess());
        assertEquals("", response.getError());
    }

    @Test
    void testErrorResponse_LongMessage() {
        String longError = "This is a very long error message that might occur during report generation " +
                "and contains detailed information about what went wrong.";
        ReportResponse response = ReportResponse.error(longError);

        assertFalse(response.isSuccess());
        assertEquals(longError, response.getError());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportResponse response1 = ReportResponse.builder()
                .success(true)
                .reportId("123")
                .build();

        ReportResponse response2 = ReportResponse.builder()
                .success(true)
                .reportId("123")
                .build();

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testToString() {
        ReportResponse response = ReportResponse.success(
                "report-123",
                "output/path.pdf",
                "pdf",
                1000L,
                "/download"
        );

        String toString = response.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("report-123"));
        assertTrue(toString.contains("pdf"));
    }

    @Test
    void testSuccessResponse_WithNullDownloadUrl() {
        ReportResponse response = ReportResponse.success(
                "report-123",
                "output/path.pdf",
                "pdf",
                1000L,
                null
        );

        assertTrue(response.isSuccess());
        assertNull(response.getDownloadUrl());
    }

    @Test
    void testBuilder_PartialData() {
        ReportResponse response = ReportResponse.builder()
                .success(true)
                .reportId("123")
                .build();

        assertTrue(response.isSuccess());
        assertEquals("123", response.getReportId());
        assertNull(response.getMessage());
        assertNull(response.getOutputPath());
    }
}
