package com.reyansh.birt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * BIRT Report Engine Application
 * 
 * Spring Boot application for generating, running and rendering BIRT reports
 * with multithreading support for scalable report generation.
 * 
 * Features:
 * - Generate reports from .rptdesign files
 * - Support multiple output formats: PDF, HTML, XLS, XLSX, DOC, DOCX, PPT, PPTX, XML
 * - Parallel report generation using thread pool
 * - Async and sync report generation endpoints
 * - Batch report generation
 * - Scalable architecture based on demand
 * 
 * @author Reyansh
 * @version 1.0.0
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BirtReportEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(BirtReportEngineApplication.class, args);
    }
}
