# Code Catcher
Static-analysis tool for understanding the architecture and potential
engineering risks of Java/Spring Boot codebases.


### Architecture Map
Controllers
↓
Services
↓
Repositories
↓
Database

### Dependency Graph
- Potential circular dependencies
- Large classes
- High coupling
- Unused dependencies
- Suspicious transactions
- Potential concurrency problems

## Technology
- Java 21
- Gradle
- JavaParser
- JUnit

## Commands
- .\gradlew.bat clean build
- .\gradlew.bat compileJava
- .\gradlew.bat run