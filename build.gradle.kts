import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

val jvmTestsOptions: String by project

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.dokka") version "1.9.20"
    id("signing")
    id("maven-publish")
}

repositories {
    mavenLocal()

    project.properties.getOrDefault("mavenInternalRepositoryUrl", "").let { _localUrl ->
        val localUrl = _localUrl as String
        if (localUrl.isEmpty()) {
            project.logger.info("Using mavenCentral repository")
            mavenCentral()
        } else {
            project.logger.info("Using internal URL repository: $localUrl")
            maven {
                url = uri(localUrl)

                credentials {
                    username = project.properties["mavenUsername"] as String?
                    password = project.properties["mavenPassword"] as String?
                }
            }
        }
    }
}

val isPublishMavenCentral = project.hasProperty("signing.gnupg.keyName")

subprojects
    .filter { it.childProjects.isEmpty() }
    .forEach {
        it.beforeEvaluate {
            apply(plugin = "signing")
            apply(plugin = "maven-publish")
            apply(plugin = "org.jetbrains.kotlin.jvm")
            apply(plugin = "org.jetbrains.dokka")

            repositories {
                mavenLocal()

                project.properties.getOrDefault("mavenInternalRepositoryUrl", "").let { _localUrl ->
                    val localUrl = _localUrl as String
                    if (localUrl.isEmpty()) {
                        project.logger.info("Using mavenCentral repository")
                        mavenCentral()
                    } else {
                        project.logger.info("Using internal URL repository: ${localUrl}")
                        maven {
                            url = uri(localUrl)

                            credentials {
                                username = project.properties["mavenUsername"] as String?
                                password = project.properties["mavenPassword"] as String?
                            }
                        }
                    }
                }
            }

            group = "com.github.inforion.common"
            version = "0.5.0"
        }

        it.afterEvaluate {
            tasks {

                withType<Test>().all {
                    useJUnitPlatform()
                    jvmArgs!!.plusAssign(jvmTestsOptions.split(" "))
                    ignoreFailures = true

                    testLogging {
                        events = setOf(PASSED, SKIPPED, FAILED)

                        showExceptions = true
                        exceptionFormat = FULL
                        showCauses = true
                        showStackTraces = true
                        // showStandardStreams = false
                    }
                }

                compileKotlin {
                    kotlinOptions.jvmTarget = "11"
                    kotlinOptions.freeCompilerArgs += listOf("-opt-in=kotlin.ExperimentalUnsignedTypes")
                }

                compileTestKotlin {
                    kotlinOptions.jvmTarget = "11"
                    kotlinOptions.freeCompilerArgs += listOf("-opt-in=kotlin.ExperimentalUnsignedTypes")
                }

                java {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }

                if (findByName("sourcesJar") == null) {
                    val sourcesJar by creating(Jar::class) {
                        dependsOn("classes")
                        archiveClassifier.set("sources")
                        from(sourceSets["main"].allSource)
                    }
                }

                if (findByName("dokkaJar") == null) {
                    val dokkaJar by creating(Jar::class) {
                        dependsOn("dokkaJavadoc")
                        archiveClassifier.set("javadoc")
                        from("dokkaJavadoc")
                    }
                }
            }

            publishing {
                val subprojectName = it.name
                val subprojectVersion = it.version as String
                val subprojectGroup = it.group as String

                repositories {
                    maven {
                        credentials {
                            username = project.properties["mavenUsername"] as String?
                            password = project.properties["mavenPassword"] as String?
                        }

                        val internalReleasesRepoUrl = (project.properties["mavenInternalReleasesUrl"] as String?)
                            ?.let { url -> uri(url) }
                        val internalSnapshotsRepoUrl = (project.properties["mavenInternalSnapshotsUrl"] as String?)
                            ?.let { url -> uri(url) }

                        val defaultReleasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                        val defaultSnapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")

                        url = if (subprojectVersion.endsWith("SNAPSHOT")) {
                            internalSnapshotsRepoUrl ?: defaultSnapshotsRepoUrl
                        } else {
                            internalReleasesRepoUrl ?: defaultReleasesRepoUrl
                        }
                    }
                }

                publications {
                    create<MavenPublication>(it.name) {
                        from(components["kotlin"])

                        if (isPublishMavenCentral)
                            artifact(tasks["dokkaJar"])

                        artifact(tasks["sourcesJar"])

                        pom {
                            groupId = subprojectGroup
                            artifactId = subprojectName
                            version = subprojectVersion

                            name.set(it.name)
                            url.set("https://github.com/inforion/kotlin-extensions")
                            description.set("Kotlin extension methods and function for different Java libraries")

                            licenses {
                                license {
                                    name.set("The MIT License")
                                    url.set("https://opensource.org/licenses/MIT")
                                }
                            }

                            developers {
                                developer {
                                    name.set("Alexei Gladkikh")
                                    email.set("gladkikhalexei@gmail.com")
                                }

                                developer {
                                    name.set("Vladimir Trishin")
                                    email.set("v.trishin@inforion.ru")
                                }

                                developer {
                                    name.set("Artur Kemurdzhian")
                                }

                                developer {
                                    name.set("Artem Simankov")
                                }

                                developer {
                                    name.set("Vladimir Davydov")
                                }
                            }

                            scm {
                                connection.set("scm:git:git://github.com/inforion/kotlin-extensions.git")
                                developerConnection.set("scm:git:ssh://github.com/inforion/kotlin-extensions.git")
                                url.set("https://github.com/inforion/kotlin-extensions")
                            }
                        }
                    }
                }
            }


            signing {
                isRequired = isPublishMavenCentral
                if (isRequired) useGpgCmd()
                sign("publishing.publications.${it.name}")
            }

            val junitVersion: String by project

            dependencies {
                testImplementation(kotlin("test"))
                testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
            }
        }
    }