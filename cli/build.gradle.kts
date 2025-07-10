plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":core"))
    implementation("info.picocli:picocli:4.7.5")
}

application {
    mainClass.set("com.glance.treetrunk.cli.MainKt")
}