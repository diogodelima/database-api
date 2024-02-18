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

    compileOnly("org.spigotmc:spigot:1.20.4-R0.1-SNAPSHOT")

    implementation("com.zaxxer:HikariCP:5.1.0")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.withType<ShadowJar>{
    relocate("com.zaxxer.hikari", "com.diogo.libs.hikari")
}