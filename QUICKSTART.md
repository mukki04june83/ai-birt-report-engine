# Quick Start Guide

## 1. Build the Project

```bash
mvn clean install
```

## 2. Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR:

```bash
java -jar target/birt-report-engine-1.0.0.jar
```

## 3. Test the Application

### Check if the application is running:

```bash
curl http://localhost:8080/api/reports/health
```

Expected response:
```json
{
  "status": "UP",
  "service": "BIRT Report Engine",
  "timestamp": "1737158400000"
}
```

### List available report templates:

```bash
curl http://localhost:8080/api/reports/templates
```

### Get supported formats:

```bash
curl http://localhost:8080/api/reports/formats
```

Expected response:
```json
["pdf", "html", "xls", "xlsx", "doc", "docx", "ppt", "pptx", "xml"]
```

## 4. Add a Report Template

Place your `.rptdesign` file in `reports/templates/` directory.

## 5. Generate Your First Report

Using cURL (Windows PowerShell):

```powershell
$body = @{
    reportName = "your_report.rptdesign"
    outputFormat = "pdf"
    parameters = @{}
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/reports/generate" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

Using cURL (Linux/Mac):

```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{"reportName":"your_report.rptdesign","outputFormat":"pdf"}'
```

## 6. Download the Generated Report

```bash
curl -O http://localhost:8080/api/reports/download/[filename].pdf
```

## Common API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/reports/health` | GET | Health check |
| `/api/reports/templates` | GET | List templates |
| `/api/reports/formats` | GET | Supported formats |
| `/api/reports/generate` | POST | Generate report (sync) |
| `/api/reports/generate/async` | POST | Generate report (async) |
| `/api/reports/generate/batch` | POST | Batch generation |
| `/api/reports/download/{file}` | GET | Download report |
| `/api/reports/status/{id}` | GET | Check status |

## Next Steps

1. Create your BIRT report designs using Eclipse BIRT Designer
2. Place them in `reports/templates/`
3. Test with different output formats
4. Scale the thread pool based on your load (see README.md)

## Need Help?

Refer to the main [README.md](README.md) for comprehensive documentation.
