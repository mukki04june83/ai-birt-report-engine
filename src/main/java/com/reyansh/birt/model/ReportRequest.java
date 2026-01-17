package com.reyansh.birt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Map;

/**
 * Request model for report generation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {

    @NotBlank(message = "Report name is required")
    private String reportName;

    @NotBlank(message = "Output format is required")
    @Pattern(regexp = "pdf|html|xls|xlsx|doc|docx|ppt|pptx|xml", 
             message = "Invalid output format. Supported: pdf, html, xls, xlsx, doc, docx, ppt, pptx, xml")
    private String outputFormat;

    private Map<String, Object> parameters;

    private String outputFileName;

    // Optional: Locale for internationalization
    private String locale;

    // Optional: Page range for PDF
    private String pageRange;
}
