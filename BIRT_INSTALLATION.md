# BIRT Runtime Installation Guide

## Overview
This application successfully uses BIRT Runtime 4.13.0 with Spring Boot by removing bundled SLF4J classes.

## ✅ Solution Implemented: Extracted BIRT Without SLF4J

The BIRT Runtime 4.13.0 fat JAR was successfully repackaged without SLF4J bindings:

### Process
1. **Extracted** `org.eclipse.birt.runtime_4.13.0-20230302.jar` (16.11 MB)
2. **Removed** `org/slf4j` directory and SLF4J Maven metadata
3. **Repackaged** as `org.eclipse.birt.runtime-noslf4j_4.13.0-20230302.jar` (16.05 MB)
4. **Installed** to Maven local repository
5. **Added** SLF4J API dependency (implementation provided by Spring Boot's Logback)

### Result
✅ **Status**: BIRT 4.13.0 running successfully with Spring Boot 2.7.18  
✅ **SLF4J Conflict**: RESOLVED  
✅ **Build**: SUCCESS  
✅ **Application**: Running on port 8080  
✅ **API**: All endpoints functional  

### Maven Configuration

```xml
<!-- BIRT without bundled SLF4J -->
<dependency>
    <groupId>org.eclipse.birt.runtime</groupId>
    <artifactId>org.eclipse.birt.runtime-noslf4j</artifactId>
    <version>4.13.0-20230302</version>
</dependency>

<!-- SLF4J API (implementation from Spring Boot Logback) -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
</dependency>
```

### Repackaging Steps (If Needed)

To recreate the no-SLF4J BIRT JAR:

```powershell
# 1. Extract the fat JAR
Set-Location "birt-runtime-4.13\extracted"
& "C:\Program Files\Java\jdk-21\bin\jar.exe" xf "..\ReportEngine\lib\org.eclipse.birt.runtime_4.13.0-20230302.jar"

# 2. Remove SLF4J classes
Remove-Item "org\slf4j" -Recurse -Force
Remove-Item "META-INF\maven\org.slf4j" -Recurse -Force -ErrorAction SilentlyContinue

# 3. Repackage without SLF4J
& "C:\Program Files\Java\jdk-21\bin\jar.exe" cf "..\org.eclipse.birt.runtime-noslf4j_4.13.0-20230302.jar" .
Set-Location "..\..\"

# 4. Install to Maven local repo
mvn install:install-file \
  -Dfile=birt-runtime-4.13\org.eclipse.birt.runtime-noslf4j_4.13.0-20230302.jar \
  -DgroupId=org.eclipse.birt.runtime \
  -DartifactId=org.eclipse.birt.runtime-noslf4j \
  -Dversion=4.13.0-20230302 \
  -Dpackaging=jar
```

## Previous Testing Results (Resolved)

| Version | Original Size | Issue | Status |
|---------|--------------|-------|--------|
| 4.8.0-20180626 | 24.44 MB | SLF4J NOPLoggerFactory conflict | ❌ Incompatible |
| 4.13.0-20230302 | 16.11 MB | SLF4J NOPLoggerFactory conflict | ❌ Incompatible |
| **4.13.0-20230302 (no SLF4J)** | **16.05 MB** | **SLF4J removed** | **✅ Working** |
# 2. Install each individually to local Maven repo
# 3. Add as separate dependencies in pom.xml

# Example:
cd birt-runtime/ReportEngine/lib
mvn install:install-file -Dfile=com.ibm.icu_58.2.0.v20170418-1837.jar \
  -DgroupId=com.ibm.icu -DartifactId=icu -Dversion=58.2.0 -Dpackaging=jar
```

**Pros:**
- Compatible with Spring Boot's class loader
- Fine-grained dependency control
- No SLF4J conflicts

**Cons:**
- Requires manual JAR identification and installation
- Many dependencies (50+ JARs)
- Complex setup process

### Option 2: Separate BIRT Service Process

Run BIRT in a separate JVM process and communicate via REST API:

```
┌──────────────────┐         ┌─────────────────┐
│  Spring Boot App │  HTTP   │  BIRT Service   │
│  (Your API)      │ ◄─────► │  (Port 9000)    │
└──────────────────┘         └─────────────────┘
```

**Pros:**
- Complete isolation, no class loading conflicts
- Can use official BIRT runtime as-is
- Easier to scale independently

**Cons:**
- More complex deployment
- Network latency
- Requires two separate applications

### Option 3: Use BIRT Viewer (Standalone)</option>

Deploy BIRT as a standalone web application and reference it:

```bash
# Deploy birt-runtime/WebViewerExample/ to Tomcat
# Access via: http://localhost:8080/birt-viewer/
```

**Pros:**
- Official BIRT deployment model
- Web-based report viewing
- No integration issues

**Cons:**
- Separate application server
- Cannot generate reports programmatically
- Less control over report lifecycle

### Option 4: Downgrade to BIRT 4.6 or Earlier

Older BIRT versions distributed individual JARs instead of fat JARs:

```xml
  },
  "components": {
    "title": {
      "text": "Sales Report 2024",
      "fontSize": 24,
      "alignment": "center",
      "includeDate": true
    },
    "tables": [{
      "datasetName": "SalesDataset",
      "title": "Sales by Region",
      "columns": [
        {
          "name": "product_name",
          "label": "Product Name",
          "width": 200,
          "dataType": "string",
          "alignment": "left"
        },
        {
          "name": "total_sales",
          "label": "Total Sales",
          "width": 150,
          "dataType": "decimal",
          "format": "$#,##0.00",
          "alignment": "right"
        }
      ],
      "enableGrouping": false,
      "includeTotals": true
    }],
    "charts": [{
      "datasetName": "SalesDataset",
      "title": "Monthly Sales Trend",
      "chartType": "bar",
      "categoryColumn": "month",
      "valueColumn": "total_sales",
      "width": 600,
      "height": 400,
      "showLegend": true
    }],
    "footer": "Generated by BIRT Report Engine",
    "pageOrientation": "portrait",
    "pageSize": "A4"
  }
}
```

## Alternative: Use Docker

For a fully containerized solution with BIRT pre-installed:

```dockerfile
FROM eclipse-temurin:21-jdk
RUN wget https://download.eclipse.org/birt/downloads/drops/R-R1-4.13.0/birt-runtime-4.13.0.zip \
    && unzip birt-runtime-4.13.0.zip -d /opt/birt
ENV BIRT_HOME=/opt/birt/birt-runtime-4.13.0
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Benefits of Mock Implementation

1. **Zero Setup**: Works immediately without BIRT installation
2. **API Testing**: Test all endpoints and request/response structures
3. **Template Generation**: Creates valid `.rptdesign` XML files
4. **Development**: Develop client applications without BIRT dependency
5. **Documentation**: Full API documentation via Swagger UI

## Upgrading to Production

When ready for production with actual report rendering:
1. Follow installation steps above
2. Replace `DynamicReportService` with BIRT-enabled version
3. Test with actual `.rptlibrary` files and data sources
4. Configure BIRT logging and performance settings
5. Set up report output caching if needed
