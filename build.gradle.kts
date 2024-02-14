plugins {
    java
    id("io.quarkus")
    id("io.freefair.lombok") version "8.1.0"
}

repositories {
    mavenCentral()
    maven(url = "https://repository.cybine.de/repository/maven-public/")
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

val cybineQuarkusUtilsVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-mutiny")
    implementation("io.quarkus:quarkus-hibernate-orm")
    implementation("io.quarkus:quarkus-rest-client-reactive")
    implementation("io.quarkus:quarkus-resteasy-reactive")
    implementation("io.quarkus:quarkus-smallrye-fault-tolerance")
    implementation("io.quarkus:quarkus-smallrye-jwt")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("io.quarkus:quarkus-rest-client-reactive-jackson")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("io.quarkus:quarkus-smallrye-reactive-messaging-rabbitmq")
    implementation("io.quarkus:quarkus-hibernate-validator")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-smallrye-reactive-messaging")
    implementation("io.quarkus:quarkus-quartz")
    implementation("io.quarkus:quarkus-websockets")
    implementation("io.quarkus:quarkus-oidc")
    implementation("io.quarkus:quarkus-jackson")
    implementation("io.quarkus:quarkus-liquibase")
    implementation("io.quarkus:quarkus-agroal")
    implementation("io.quarkus:quarkus-arc")

    implementation("de.cybine.quarkus:action-processor:${cybineQuarkusUtilsVersion}")
    implementation("de.cybine.quarkus:api-query:${cybineQuarkusUtilsVersion}")
    implementation("de.cybine.quarkus:common:${cybineQuarkusUtilsVersion}")
    implementation("de.cybine.quarkus:datasource-query:${cybineQuarkusUtilsVersion}")
    implementation("de.cybine.quarkus:type-converter:${cybineQuarkusUtilsVersion}")

    implementation("com.fasterxml.uuid:java-uuid-generator:4.1.0")
    implementation("org.mindrot:jbcrypt:0.4")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "de.cybine.factory"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
