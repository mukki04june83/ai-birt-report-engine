package com.reyansh.birt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for report generation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

    private boolean success;
    
    private String message;
    
    private String reportId;
    
    private String outputPath;
    
    private String outputFormat;
    
    private long generationTimeMs;
    
    private String downloadUrl;
    
    private String error;

    public static ReportResponse success(String reportId, String outputPath, 
                                        String format, long timeMs, String downloadUrl) {
        return ReportResponse.builder()
                .success(true)
                .message("Report generated successfully")
                .reportId(reportId)
                .outputPath(outputPath)
                .outputFormat(format)
                .generationTimeMs(timeMs)
                .downloadUrl(downloadUrl)
                .build();
    }

    public static ReportResponse error(String error) {
        return ReportResponse.builder()
                .success(false)
                .message("Report generation failed")
                .error(error)
                .build();
    }
}
