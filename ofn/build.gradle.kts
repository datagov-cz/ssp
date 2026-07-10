plugins {
    kotlin("jvm") version "1.9.0"
    id("io.freefair.lombok") version "8.7.1"
}

group = "cz.gov.ssp.ofn"
version = "1.0-SNAPSHOT"

tasks.test {
    maxHeapSize = "4G"
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.jena:jena-shacl:5.0.0")
    implementation("org.topbraid:shacl:1.4.3")
    implementation("org.slf4j:slf4j-simple:1.7.32")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
}