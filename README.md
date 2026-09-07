# Code Catcher

Code Catcher is a Java command-line static analysis tool for inspecting Java and Spring projects.

## Requirements

- Java 21

## Usage

```bash
java -jar code-catcher.jar <project-folder>
```

Example:

```bash
java -jar code-catcher.jar E:\sample
```

## Features

- Java source file scanning
- Java class and interface extraction
- Method metadata extraction
- Spring Controller detection
- Spring Service detection
- Spring Repository detection
- Dependency graph generation
- Circular dependency detection
- Architecture rule violation detection
- Code smell detection
- N+1 query pattern detection
- Concurrent Java source parsing and model building
- Indexed dependency resolution
- Concurrent analysis detectors
- Total project inspection time reporting

## Build

### Windows

```powershell
.\gradlew.bat clean build
```

### Linux / macOS

```bash
./gradlew clean build
```

The generated JAR is:

```text
build/libs/code-catcher.jar
```

## Output

Code Catcher prints the discovered project structure and static analysis results directly to the console.

## Version

**v0.1.0**

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE).