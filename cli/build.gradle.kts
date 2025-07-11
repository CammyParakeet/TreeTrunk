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
    }
}