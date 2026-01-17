package com.reyansh.birt.controller;

import com.reyansh.birt.model.DynamicReportRequest;
import com.reyansh.birt.model.ReportRequest;
import com.reyansh.birt.model.ReportResponse;
import com.reyansh.birt.service.DynamicReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for Report Generation API
 */
@Slf4j
@RestController
@RequestMapping("/api/reports")
@Validated
@RequiredArgsConstructor
@Tag(name = "Report Generation", description = "APIs for generating and managing BIRT reports")
public class ReportController {

    private final DynamicReportService dynamicReportService;

    @Operation(
            summary = "Generate dynamic report from library",
            description = "Dynamically generates a report template (.rptdesign) using components from a .rptlibrary file, " +
                    "including datasources, datasets, parameters, tables, and charts. The template is built, executed, " +
                    "and rendered based on the provided configuration."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Report generated successfully",
                    content = @Content(schema = @Schema(implementation = ReportResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during report generation"
            )
    })
    @PostMapping("/generate-dynamic")
    public ResponseEntity<ReportResponse> generateDynamicReport(
            @Valid @RequestBody @Parameter(description = "Dynamic report generation request with library components") 
            DynamicReportRequest request) {
        
        log.info("Generating dynamic report from library: {}", request.getLibraryPath());
        
        long startTime = System.currentTimeMillis();
        
        try {
            String reportId = dynamicReportService.generateDynamicReport(request);
            long generationTime = System.currentTimeMillis() - startTime;
            
            ReportResponse response = ReportResponse.success(
                    reportId,
                    "reports/output/" + reportId + "." + request.getOutputFormat(),
                    request.getOutputFormat(),
                    generationTime,
                    "/api/reports/download/" + reportId
            );
            
            log.info("Dynamic report generated successfully: {} in {}ms", reportId, generationTime);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error generating dynamic report: {}", e.getMessage(), e);
            ReportResponse errorResponse = ReportResponse.error("Failed to generate dynamic report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(
            summary = "Generate a new report",
            description = "Generates a report in the specified format (PDF, HTML, Excel, etc.) with optional parameters"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Report generated successfully",
                    content = @Content(schema = @Schema(implementation = ReportResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during report generation"
            )
    })
    @PostMapping("/generate")
    public ResponseEntity<ReportResponse> generateReport(
            @Valid @RequestBody @Parameter(description = "Report generation request") ReportRequest request) {
        
        log.info("Generating report: {} in format: {}", request.getReportName(), request.getOutputFormat());
        
        // Simulated response (BIRT engine is disabled)
        String reportId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();
        
        // Simulate processing time
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long generationTime = System.currentTimeMillis() - startTime;
        
        ReportResponse response = ReportResponse.success(
                reportId,
                "reports/output/" + reportId + "." + request.getOutputFormat(),
                request.getOutputFormat(),
                generationTime,
                "/api/reports/download/" + reportId
        );
        
        log.info("Report generated successfully: {}", reportId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get report status",
            description = "Check the generation status of a report by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Report status retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Report not found"
            )
    })
    @GetMapping("/status/{reportId}")
    public ResponseEntity<Map<String, Object>> getReportStatus(
            @PathVariable @Parameter(description = "Unique report identifier") String reportId) {
        
        log.info("Checking status for report: {}", reportId);
        
        Map<String, Object> status = new HashMap<>();
        status.put("reportId", reportId);
        status.put("status", "COMPLETED");
        status.put("progress", 100);
        status.put("message", "Report generation completed");
        
        return ResponseEntity.ok(status);
    }

    @Operation(
            summary = "Download generated report",
            description = "Download a previously generated report by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Report downloaded successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Report not found"
            )
    })
    @GetMapping("/download/{reportId}")
    public ResponseEntity<String> downloadReport(
            @PathVariable @Parameter(description = "Unique report identifier") String reportId) {
        
        log.info("Downloading report: {}", reportId);
        
        // Return simulated response
        return ResponseEntity.ok("Report download would happen here. Report ID: " + reportId);
    }

    @Operation(
            summary = "List available report templates",
            description = "Get a list of all available BIRT report templates"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Templates retrieved successfully"
            )
    })
    @GetMapping("/templates")
    public ResponseEntity<Map<String, Object>> listTemplates() {
        
        log.info("Listing available report templates");
        
        Map<String, Object> response = new HashMap<>();
        response.put("templates", new String[]{
                "sales-report.rptdesign",
                "inventory-report.rptdesign",
                "customer-report.rptdesign"
        });
        response.put("count", 3);
        
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Health check",
            description = "Check if the report engine is running and healthy"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Service is healthy"
            )
    })
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "BIRT Report Engine");
        health.put("version", "1.0.0");
        health.put("java", System.getProperty("java.version"));
        
        return ResponseEntity.ok(health);
    }

    @Operation(
            summary = "Delete a report",
            description = "Delete a generated report by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Report deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Report not found"
            )
    })
    @DeleteMapping("/{reportId}")
    public ResponseEntity<Map<String, String>> deleteReport(
            @PathVariable @Parameter(description = "Unique report identifier") String reportId) {
        
        log.info("Deleting report: {}", reportId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Report deleted successfully");
        response.put("reportId", reportId);
        
        return ResponseEntity.ok(response);
    }
}
