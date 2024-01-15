## Dev Environment

### VsCode 1.83.1
- Extension Pack for Java v0.25.14
- Gradle for Java v3.13.5
- Lombok Annotations Support for VS Code v1.1.0
- Red Hat Dependency Analytics v0.7.0
- Spring Boot Extension Pack v0.2.1
### jdk-21.0.0.35-hotspot
- Spring Boot Starter 3.2.1
- Gradle 8.5
### settings.json
```json
    "java.compile.nullAnalysis.nonnull": ["javax.annotation.Nonnull", "org.eclipse.jdt.annotation.NonNull", "org.springframework.lang.NonNull"],
    "java.configuration.runtimes": [
        {
            "default": true,
            "name": "JavaSE-21",
            "path": "C:\\Program Files\\Eclipse Adoptium\\jdk-21.0.1.12-hotspot"
        }
    ],
```
## Build
### Edit Environment Variables
```bash
JAVA_HOME
```
### PowerShell
```bash
.\gradlew build -x test
```