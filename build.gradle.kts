plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("com.google.auth:google-auth-library-oauth2-http:1.21.0")
	implementation("com.google.apis:google-api-services-gmail:v1-rev110-1.25.0")
	implementation("com.google.api-client:google-api-client:1.31.5")
	implementation("com.google.oauth-client:google-oauth-client-jetty:1.31.5")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("jakarta.mail:jakarta.mail-api:2.1.2")
	implementation("org.eclipse.angus:angus-mail:2.0.2")
	implementation("com.google.http-client:google-http-client-jackson2:1.41.5")
	implementation("com.google.oauth-client:google-oauth-client-java6:1.31.5")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
