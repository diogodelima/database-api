import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "com.diogo"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {

    implementation("org.xerial:sqlite-jdbc:3.46.0.0")
    implementation("com.mysql:mysql-connector-j:8.4.0")
    implementation("com.zaxxer:HikariCP:5.1.0")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<ShadowJar>{
    relocate("com.zaxxer.hikari", "com.diogo.libs.hikari")
}