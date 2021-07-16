import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("org.jetbrains.dokka") version "1.4.0"
    id("signing")
    id("maven")
    id("maven-publish")
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven { url = uri("https://jitpack.io") }
}

val isPublishMavenCentral = project.hasProperty("signing.gnupg.keyName")

subprojects
    .filter { it.childProjects.isEmpty() }
    .forEach {
        it.beforeEvaluate {
            apply(plugin = "signing")
            apply(plugin = "maven")
            apply(plugin = "maven-publish")
            apply(plugin = "org.jetbrains.kotlin.jvm")
            apply(plugin = "org.jetbrains.dokka")

            repositories {
                mavenLocal()
                mavenCentral()
                jcenter()
                maven { url = uri("https://jitpack.io") }
            }

            group = "com.github.inforion.common"
            version = "0.4.0"
        }

        it.afterEvaluate {
            tasks {

                filterIsInstance<Test>()
                    .forEach { task ->
                        task.jvmArgs!!.plusAssign(
                            "-server " +
                                    "-Xms2G " +
                                    "-Xmx5G " +
                                    "-XX:MaxDirectMemorySize=2g " +
                                    "-XX:+UseParallelGC " +
                                    "-XX:SurvivorRatio=6 " +
                                    "-XX:-UseGCOverheadLimit"
                                        .split(" ")
                        )

                        task.testLogging {
                            events = setOf(PASSED, SKIPPED, FAILED)

                            showExceptions = true
                            exceptionFormat = FULL
                            showCauses = true
                            showStackTraces = true
                            // showStandardStreams = false
                        }
                    }

                filterIsInstance<KotlinCompile>().forEach { task ->
                    task.kotlinOptions { jvmTarget = "11" }
                }

                if (findByName("sourcesJar") == null) {
                    val sourcesJar by creating(Jar::class) {
                        dependsOn("classes")
                        classifier = "sources"
                        from(sourceSets["main"].allSource)
                    }
                }

                if (findByName("dokkaJar") == null) {
                    val dokkaJar by creating(Jar::class) {
                        dependsOn("dokkaJavadoc")
                        classifier = "javadoc"
                        from("dokkaJavadoc")
                    }
                }
            }

            publishing {
                val subprojectName = it.name as String
                val subprojectVersion = it.version as String
                val subprojectGroup = it.group as String

                repositories {
                    maven {
                        credentials {
                            username = project.properties["mavenUsername"] as String
                            password = project.properties["mavenPassword"] as String
                        }

                        val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                        val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
                        url = if (subprojectVersion.endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
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
                                    organization.set("INFORION, LLC")
                                }

                                developer {
                                    name.set("Artur Kemurdzhian")
                                    organization.set("INFORION, LLC")
                                }

                                developer {
                                    name.set("Artem Simankov")
                                    organization.set("INFORION, LLC")
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

            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
                implementation("org.jetbrains.kotlin:kotlin-reflect")
                implementation("org.jetbrains.kotlin:kotlin-test")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")

                implementation("joda-time:joda-time:2.10.10")

                implementation("com.fasterxml.jackson.datatype:jackson-datatype-joda:2.12.4")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.4")

                testImplementation("junit:junit:4.13.2")
                testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
                testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
                testImplementation("org.assertj:assertj-core:3.20.2")
            }
        }
    }