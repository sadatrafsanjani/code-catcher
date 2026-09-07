plugins {
    id("java")
    id("application")
}

group = "com.catcher"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation("com.github.javaparser:javaparser-core:3.28.2")
    implementation("com.github.javaparser:javaparser-core-serialization:3.28.2")

    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("com.catcher.Main")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveFileName.set("code-catcher.jar")

    manifest {
        attributes["Main-Class"] = "com.catcher.Main"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
}
