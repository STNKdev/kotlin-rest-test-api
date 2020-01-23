import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.2.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	//id("org.flywaydb.flyway") version "6.1.4"

	id("org.asciidoctor.convert") version "2.4.0"

	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
	kotlin("plugin.jpa") version "1.3.61"
	kotlin("plugin.allopen") version "1.3.61"
}

group = "ru.stnk"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	// Spring jpa, crud repository
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	// Spring Web
	implementation("org.springframework.boot:spring-boot-starter-web") {
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
	}
	// Embedded Undertow
	implementation("org.springframework.boot:spring-boot-starter-undertow")
	// Spring Security
	implementation("org.springframework.boot:spring-boot-starter-security")
	// Spring Mail
	implementation("org.springframework.boot:spring-boot-starter-mail")
	// Spring WebSocket
	implementation("org.springframework.boot:spring-boot-starter-websocket") {
		// WTF?! Этой зависимости нет в dependencies tree, но без нее не отключается tomcat
		// на stackoverflow была подсказка
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
	}
	// Kotlin modules
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	//runtimeOnly("com.h2database:h2")
	// Spring Session
	implementation("org.springframework.session:spring-session-jdbc")
	// PostgreSQL
	runtimeOnly("org.postgresql:postgresql")
	// Test dependencies and Spring Test
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	//testImplementation("com.ninja-squad:springmockk:1.1.3")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

	// Spring RESTDocs
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor")

	// Flyway
	implementation("org.flywaydb:flyway-core")

}

/*tasks.withType<Test> {
	useJUnitPlatform()
}*/

// В телеграм чате gradle подсказали что без configureEach
// будет создавать test задачи даже когда они не нужны
/*tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}*/

allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.Embeddable")
	annotation("javax.persistence.MappedSuperclass")
}

tasks {

	withType<Test>().configureEach {
		useJUnitPlatform()
	}

	withType<KotlinCompile>().configureEach {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "1.8"
		}
	}

	asciidoctor {

		sourceDir = file("src/test/resources/docs")
		/*sources(delegateClosureOf<PatternSet> {
			include("toplevel.adoc", "another.adoc", "third.adoc")
		})*/
		outputDir = file("build/docs")

		/*attributes(
			mapOf(
				"snippets" to file("build/generated-snippets")
			)
		)*/
	}
}