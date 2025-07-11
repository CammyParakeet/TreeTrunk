import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":core"))
    implementation("info.picocli:picocli:4.7.5")
    annotationProcessor("info.picocli:picocli-codegen:4.7.5")
}

application {
    mainClass.set("com.glance.treetrunk.cli.MainKt")
}

tasks {
    named<Jar>("jar") {
        manifest {
            attributes["Implementation-Version"] = project.version
        }
    }
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes["Implementation-Version"] = project.version
        }
        archiveBaseName.set("treetrunk-cli")
        archiveClassifier.set("all")
        archiveVersion.set("")
    }

    register("installLocalTreeTrunk") {
        group = "distribution"
        description = "Installs TreeTrunk locally using install-treetrunk-dev"

        dependsOn(shadowJar)

        doLast {
            val os = System.getProperty("os.name").lowercase()

            val scriptType = when {
                os.contains("win") -> "ps1"
                os.contains("mac") ||
                    os.contains("nix") ||
                    os.contains("nux") -> "sh"
                else -> throw GradleException("Unsupported OS: $os")
            }
            val script = rootProject.file("scripts/install/install-treetrunk-dev.$scriptType")
            if (!script.exists()) {
                throw GradleException("Install script not found: $script")
            }

            if (os.contains("win")) {
                exec {
                    commandLine("powershell", "-ExecutionPolicy", "Bypass", "-File", script.absolutePath)
                }
            } else {
                exec {
                    commandLine("bash", script.absolutePath)
                }
            }
        }
    }
}