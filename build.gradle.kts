plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "cn.qfys521"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation("cn.chuanwise.xiaoming:xiaomingbot:20250101-210305:all")
    implementation("top.mrxiaom.mirai:overflow-core:1.0.1.541-e0158ba-SNAPSHOT")
    implementation("net.mamoe:mirai-core-api-jvm:2.16.0")

    compileOnly("org.slf4j:slf4j-reload4j:2.0.16")
    compileOnly("org.slf4j:slf4j-api:2.0.16")
    compileOnly("org.projectlombok:lombok:1.18+")
    annotationProcessor("org.projectlombok:lombok:1.18+")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
}
tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "cn.qfys521.XiaoMingBotTerminalMain"
        attributes["Build-JDK-Version"] = java.sourceCompatibility.majorVersion
        attributes["Group"] = group
        attributes["Build-By"] = "qfys521"
    }
}