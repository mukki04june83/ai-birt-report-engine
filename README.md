# BIRT Report Engine - Spring Boot Application

A comprehensive Spring Boot application for generating, running, and rendering BIRT (Business Intelligence and Reporting Tools) reports with multithreading support.

## Features

- ✅ Generate reports from `.rptdesign` files using BIRT Runtime 4.8.0
- ✅ Multiple output formats: PDF, HTML, XLS, XLSX, DOC, DOCX, PPT, PPTX, XML
- ✅ Parallel report generation using Java multithreading
- ✅ Scalable thread pool configuration (10-50 threads)
- ✅ Synchronous and asynchronous report generation
- ✅ Batch report generation
- ✅ REST API endpoints
- ✅ Report download functionality
- ✅ Template management

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- BIRT Runtime 4.8.0-20180626

## Project Structure

```
birt-report-engine/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/reyansh/birt/
│   │   │       ├── BirtReportEngineApplication.java
│   │   │       ├── config/
│   │   │       │   └── BirtEngineConfiguration.java
│   │   │       ├── controller/
│   │   │       │   └── ReportController.java
│   │   │       ├── model/
│   │   │       │   ├── ReportRequest.java
│   │   │       │   └── ReportResponse.java
│   │   │       └── service/
│   │   │           └── ReportGenerationService.java
│   │   └── resources/
│   │       └── application.properties
├── reports/
│   ├── templates/    (Place your .rptdesign files here)
│   └── output/       (Generated reports will be saved here)
├── logs/
└── pom.xml
```

## Configuration

### application.properties

Key configurations for scalability:

```properties
# Thread Pool Configuration
spring.task.execution.pool.core-size=10        # Base threads
spring.task.execution.pool.max-size=50         # Max threads
spring.task.execution.pool.queue-capacity=100  # Queue size

# Report Configuration
birt.report.directory=reports/templates
birt.output.directory=reports/output
birt.log.directory=logs

# Performance Tuning
server.tomcat.threads.max=200
```

### Thread Pool Tuning

Adjust based on your requirements:

- **Low load** (1-5 concurrent reports): core-size=5, max-size=10
- **Medium load** (5-20 concurrent reports): core-size=10, max-size=30
- **High load** (20+ concurrent reports): core-size=20, max-size=100

## Installation & Setup

### 1. Clone or download the project

```bash
cd c:\Users\mukes\Development\Reyansh
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Create required directories

```bash
mkdir reports\templates
mkdir reports\output
mkdir logs
```

### 4. Add your BIRT report templates

Place your `.rptdesign` files in the `reports/templates` directory.

### 5. Run the application

```bash
mvn spring-boot:run
```

Or run the JAR file:

```bash
java -jar target/birt-report-engine-1.0.0.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### 1. Generate Report (Synchronous)

**POST** `/api/reports/generate`

```json
{
  "reportName": "sample_report.rptdesign",
  "outputFormat": "pdf",
  "parameters": {
    "param1": "value1",
    "param2": "value2"
  },
  "outputFileName": "my_report"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Report generated successfully",
  "reportId": "uuid",
  "outputPath": "reports/output/my_report.pdf",
  "outputFormat": "pdf",
  "generationTimeMs": 1234,
  "downloadUrl": "/api/reports/download/my_report.pdf"
}
```

### 2. Generate Report (Asynchronous)

**POST** `/api/reports/generate/async`

Returns immediately, report generates in background.

```json
{
  "reportName": "large_report.rptdesign",
  "outputFormat": "xlsx",
  "parameters": {}
}
```

**Response:**
```json
{
  "message": "Report generation started",
  "status": "PROCESSING"
}
```

### 3. Batch Report Generation

**POST** `/api/reports/generate/batch`

Generate multiple reports in parallel:

```json
[
  {
    "reportName": "report1.rptdesign",
    "outputFormat": "pdf"
  },
  {
    "reportName": "report2.rptdesign",
    "outputFormat": "xlsx"
  }
]
```

**Response:**
```json
{
  "totalReports": 2,
  "successCount": 2,
  "failureCount": 0,
  "results": [...]
}
```

### 4. Download Report

**GET** `/api/reports/download/{fileName}`

Example: `GET /api/reports/download/my_report.pdf`

### 5. List Templates

**GET** `/api/reports/templates`

Returns list of available `.rptdesign` files.

### 6. Check Status

**GET** `/api/reports/status/{reportId}`

Check async report generation status.

### 7. Supported Formats

**GET** `/api/reports/formats`

Returns: `["pdf", "html", "xls", "xlsx", "doc", "docx", "ppt", "pptx", "xml"]`

### 8. Health Check

**GET** `/api/reports/health`

## Supported Output Formats

| Format | Extension | Description |
|--------|-----------|-------------|
| PDF | .pdf | Adobe Portable Document Format |
| HTML | .html | Web page format |
| XLS | .xls | Microsoft Excel 97-2003 |
| XLSX | .xlsx | Microsoft Excel 2007+ |
| DOC | .doc | Microsoft Word 97-2003 |
| DOCX | .docx | Microsoft Word 2007+ |
| PPT | .ppt | Microsoft PowerPoint 97-2003 |
| PPTX | .pptx | Microsoft PowerPoint 2007+ |
| XML | .xml | Extensible Markup Language |

## Examples

### Using cURL

#### Generate PDF Report

```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d "{\"reportName\":\"sample.rptdesign\",\"outputFormat\":\"pdf\"}"
```

#### Generate Excel Report with Parameters

```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d "{\"reportName\":\"sales.rptdesign\",\"outputFormat\":\"xlsx\",\"parameters\":{\"year\":2024,\"quarter\":\"Q1\"}}"
```

#### Download Report

```bash
curl -O http://localhost:8080/api/reports/download/my_report.pdf
```

### Using Postman

1. Create new POST request to `http://localhost:8080/api/reports/generate`
2. Set Headers: `Content-Type: application/json`
3. Body (raw JSON):
```json
{
  "reportName": "your_report.rptdesign",
  "outputFormat": "pdf",
  "parameters": {
    "startDate": "2024-01-01",
    "endDate": "2024-12-31"
  }
}
```

## Performance Optimization

### 1. JVM Settings

For high-load scenarios, configure JVM:

```bash
java -Xms2g -Xmx4g -XX:+UseG1GC -jar birt-report-engine-1.0.0.jar
```

### 2. Thread Pool Tuning

Edit `application.properties`:

```properties
# For high concurrency (50+ reports)
spring.task.execution.pool.core-size=20
spring.task.execution.pool.max-size=100
spring.task.execution.pool.queue-capacity=200
```

### 3. Database Connection Pooling

If your reports use database connections, configure HikariCP:

```properties
spring.datasource.hikari.maximum-pool-size=50
spring.datasource.hikari.minimum-idle=10
```

## Monitoring & Logging

Logs are stored in `logs/birt-report-engine.log`

View real-time logs:

```bash
tail -f logs/birt-report-engine.log
```

Check thread pool status via actuator (add spring-boot-starter-actuator):

```bash
curl http://localhost:8080/actuator/metrics/executor.pool.size
```

## Troubleshooting

### Issue: Report template not found

**Solution:** Ensure `.rptdesign` file is in `reports/templates` directory.

### Issue: Out of Memory

**Solution:** Increase JVM heap size:
```bash
java -Xmx4g -jar birt-report-engine-1.0.0.jar
```

### Issue: Thread pool exhausted

**Solution:** Increase max pool size in `application.properties`:
```properties
spring.task.execution.pool.max-size=100
```

### Issue: BIRT dependencies not found

**Solution:** Rebuild with Maven:
```bash
mvn clean install -U
```

## Docker Support

Create `Dockerfile`:

```dockerfile
FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/birt-report-engine-1.0.0.jar app.jar
COPY reports reports
RUN mkdir -p logs
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t birt-report-engine .
docker run -p 8080:8080 -v $(pwd)/reports:/app/reports birt-report-engine
```

## Production Deployment

### 1. Build for production

```bash
mvn clean package -DskipTests
```

### 2. Run as service (Linux)

Create `/etc/systemd/system/birt-report.service`:

```ini
[Unit]
Description=BIRT Report Engine
After=network.target

[Service]
User=birtuser
ExecStart=/usr/bin/java -jar /opt/birt/birt-report-engine-1.0.0.jar
SuccessExitStatus=143
Restart=always

[Install]
WantedBy=multi-user.target
```

Enable and start:

```bash
sudo systemctl enable birt-report.service
sudo systemctl start birt-report.service
```

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please create an issue in the repository.

## Version History

- **1.0.0** - Initial release
  - BIRT Runtime 4.8.0 integration
  - Multithreading support
  - Multiple output format support
  - REST API endpoints
  - Async report generation

## Author

Reyansh Development Team
