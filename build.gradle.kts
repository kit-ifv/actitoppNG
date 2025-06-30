plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.9.10"
}

group = "edu.kit.ifv.mobitopp"
version = if (project.hasProperty("next-version")) {
    project.property("next-version") as String
} else {
    "0.0-SNAPSHOT"
}
version = "0.9.2"

tasks.wrapper {
    gradleVersion = "6.3"
}

repositories {
    maven { url = uri("https://nexus.ifv.kit.edu/repository/maven-releases/") }
    maven { url = uri("https://nexus.ifv.kit.edu/repository/maven-central/") }
    maven { url = uri("https://nexus.ifv.kit.edu/repository/maven-snapshots/") }
    maven("https://packages.jetbrains.team/maven/p/kds/kotlin-ds-maven")
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("edu.kit.ifv.mobitopp:kotlin-units:1.2.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    implementation("edu.kit.ifv.mobitopp:discrete-choice:1.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}



tasks.test {
    useJUnitPlatform()
}
tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("dokka"))

    dokkaSourceSets {
        named("main") {
            includes.from("src/main/kotlin/com/example/mypackage/package.md") // optional
            perPackageOption {
                matchingRegex.set("com\\.example\\.internal.*")
                suppress.set(true) // Skip internal packages
            }
        }
    }
}

publishing {
    publications {
        register("mavenData", MavenPublication::class) {
            from(components["kotlin"])
        }
    }
    repositories {
        maven {
            url = uri("https://nexus.ifv.kit.edu/repository/maven-releases/")

            credentials {
                username = project.findProperty("nexusUsername") as String?
                password = project.findProperty("nexusPassword") as String?
            }
        }
    }
}
