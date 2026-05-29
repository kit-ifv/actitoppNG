plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
    id("maven-publish")
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("signing")
    id("me.champeau.jmh") version "0.7.3"
}

group = "edu.kit.ifv.mobitopp"


tasks.wrapper {
    gradleVersion = "9.5.0"
}

kotlin {
    jvmToolchain(25)
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

repositories {
    maven("https://packages.jetbrains.team/maven/p/kds/kotlin-ds-maven")
    mavenCentral()
}

dependencies {
    implementation("edu.kit.ifv.mobitopp:kotlin-units:1.0.0")
    implementation("edu.kit.ifv.mobitopp:discrete-choice:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:1.37")

}

tasks.test {
    useJUnitPlatform()
}

if (checkProperty("doPublish")) {
    /* mobiTopp publishing process (see .gitlab-ci.yml)
        * Parameters such as "doPublish" must be passed in gradle command:
        *  - ./gradlew <TASKS> -PdoPublish=true -Pparam=value...
        * Lookup of parameters doPublish and isRelease returns true if they are specified and their value reads "true".
        * Other required parameters must be specified, otherwise an error is thrown.
        *
        * The pipeline build version is used as the published artifacts version string.
        *  - uses parameter: "buildVersion"
        *
        * Every merge on main is published to local repo: see deploy-job
        *  - checks: doPublish=true, isRelease=false
        *  - gradle task: publish
        *  - requires parameters: "localUrl", "localRepoUser" and "localRepoPassword"
        *
        * Public releases must be published manually:
        *  - checks: doPublish=true, isRelease=true
        *  - gradle tasks: publishToSonatype closeSonatypeStagingRepository
        *  - requires parameters: sonatypeUsername, sonatypePassword signing.keyId signing.password signing.secretKeyRingFile
        */

    project.version = requireProperty("buildVersion")
    println("Setup publishing configuration for ${group}:${project.name}:${version}.")

    val githubURL: String = "github.com/kit-ifv/actitoppNG"
    val projectDescription: String = "actiTopp next generation is a modernized version of actiTopp: a model to generate week activity schedules"

    publishing {

        publications {

            create<MavenPublication>("mavenData") {
                from(components["java"])
                groupId = group.toString()
                artifactId = project.name
                version = project.version.toString()

                pom {
                    name.set(project.name)
                    description.set(projectDescription)
                    url.set("https://$githubURL")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://mit-license.org")
                        }
                    }

                    developers {
                        developer {
                            id.set("id")
                            name.set("name")
                            email.set("mail")
                        }
                    }

                    scm {
                        connection.set("scm:git:git:https://$githubURL.git")
                        developerConnection.set("scm:git:ssh://git@$githubURL.git")
                        url.set("https://$githubURL")
                    }
                }
            }

        }

        repositories {
            if (checkProperty("isRelease")) {
                println("Activate: publish public release!")
                
                signing {
                    sign(publishing.publications)
                }

                nexusPublishing {
                    repositories {
                        // see https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/#configuration
                        sonatype {
                            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
                            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
                        }
                    }
                }

            } else {
                println("Activate: publish local build!")
                maven {
                    name = "LocalRepo"
                    url = uri(requireProperty("localUrl"))
                    credentials {
                        username = requireProperty("localRepoUser")
                        password = requireProperty("localRepoPassword")
                    }
                }
            }
        }

    }

}


fun requireProperty(property: String, orElse: String? = null): String =
    requireNotNull(project.findProperty(property) as? String ?: orElse) {
        "Could not find property '$property'. Please check the gradle command args. It should contain:\n" +
                "    ./gradlew ... -P$property=<VALUE> ..."
    }

fun checkProperty(property: String): Boolean = project.hasProperty(property) && project.property(property) == "true"

