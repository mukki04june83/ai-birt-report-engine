package com.reyansh.birt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * Request model for dynamic report generation from library
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dynamic report generation request with library components")
public class DynamicReportRequest {

    @NotBlank(message = "Library path is required")
    @Schema(description = "Path to the .rptlibrary file containing datasources, datasets, and parameters", example = "reports/library/common.rptlibrary")
    private String libraryPath;

    @NotBlank(message = "Report name is required")
    @Schema(description = "Name for the generated report", example = "sales-report")
    private String reportName;

    @NotBlank(message = "Output format is required")
    @Schema(description = "Output format for the report", example = "pdf", allowableValues = {"pdf", "html", "xls", "xlsx", "doc", "docx"})
    private String outputFormat;

    @Schema(description = "Data source name from the library to use")
    private String dataSourceName;

    @NotEmpty(message = "At least one dataset is required")
    @Schema(description = "List of dataset names from the library to include")
    private List<String> datasetNames;

    @Schema(description = "Parameters to use in the report with their values")
    private Map<String, Object> parameters;

    @Schema(description = "Report components configuration")
    private ReportComponents components;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Configuration for report components")
    public static class ReportComponents {
        
        @Schema(description = "Title section configuration")
        private TitleSection title;
        
        @Schema(description = "List of table configurations")
        private List<TableConfig> tables;
        
        @Schema(description = "List of chart configurations")
        private List<ChartConfig> charts;
        
        @Schema(description = "Footer text")
        private String footer;
        
        @Schema(description = "Page orientation", allowableValues = {"portrait", "landscape"})
        private String pageOrientation;
        
        @Schema(description = "Page size", allowableValues = {"A4", "Letter", "Legal"})
        private String pageSize;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Title section configuration")
    public static class TitleSection {
        
        @Schema(description = "Report title text", example = "Monthly Sales Report")
        private String text;
        
        @Schema(description = "Title font size", example = "24")
        private Integer fontSize;
        
        @Schema(description = "Title alignment", allowableValues = {"left", "center", "right"})
        private String alignment;
        
        @Schema(description = "Include date in title")
        private Boolean includeDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Table configuration")
    public static class TableConfig {
        
        @Schema(description = "Dataset name to use for this table", example = "SalesDataset")
        private String datasetName;
        
        @Schema(description = "Table title", example = "Sales by Region")
        private String title;
        
        @Schema(description = "Columns to include in the table")
        private List<ColumnConfig> columns;
        
        @Schema(description = "Enable grouping")
        private Boolean enableGrouping;
        
        @Schema(description = "Group by column name")
        private String groupByColumn;
        
        @Schema(description = "Include totals row")
        private Boolean includeTotals;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Column configuration")
    public static class ColumnConfig {
        
        @Schema(description = "Column name from dataset", example = "product_name")
        private String name;
        
        @Schema(description = "Display label", example = "Product Name")
        private String label;
        
        @Schema(description = "Column width in pixels", example = "150")
        private Integer width;
        
        @Schema(description = "Data type", allowableValues = {"string", "integer", "decimal", "date"})
        private String dataType;
        
        @Schema(description = "Format pattern", example = "#,##0.00")
        private String format;
        
        @Schema(description = "Horizontal alignment", allowableValues = {"left", "center", "right"})
        private String alignment;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Chart configuration")
    public static class ChartConfig {
        
        @Schema(description = "Dataset name to use for this chart", example = "SalesDataset")
        private String datasetName;
        
        @Schema(description = "Chart title", example = "Sales Trend")
        private String title;
        
        @Schema(description = "Chart type", allowableValues = {"bar", "line", "pie", "area"})
        private String chartType;
        
        @Schema(description = "Category/X-axis column", example = "month")
        private String categoryColumn;
        
        @Schema(description = "Value/Y-axis column", example = "total_sales")
        private String valueColumn;
        
        @Schema(description = "Chart width in pixels", example = "600")
        private Integer width;
        
        @Schema(description = "Chart height in pixels", example = "400")
        private Integer height;
        
        @Schema(description = "Show legend")
        private Boolean showLegend;
    }
}
