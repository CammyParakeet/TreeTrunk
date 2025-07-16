package com.glance.treetrunk.core.test.strategy.preset

import com.glance.treetrunk.core.strategy.Strategy
import com.glance.treetrunk.core.strategy.StrategyLoader
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.IOException
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively

@OptIn(ExperimentalPathApi::class)
class StrategyLoaderTest : StringSpec({

    val tempDir = createTempDirectory("strategytest")

    afterSpec {
        tempDir.deleteRecursively()
    }

    "should load IGNORE strategy from resource" {
        val lines = StrategyLoader.loadStrategyFile("gradle", Strategy.IGNORE)
        lines shouldBe listOf(
            "build/",
            ".gradle/",
            ".idea/",
            "*.iml",
            "out/"
        )
    }

    "should load INCLUDE strategy from resource" {
        val lines = StrategyLoader.loadStrategyFile("kotlin", Strategy.INCLUDE)
        lines?.isNotEmpty() shouldBe true
    }

    "should return null when resource strategy is missing" {
        val result = StrategyLoader.loadStrategyFile("nonexistent", Strategy.IGNORE)
        result shouldBe null
    }

    "should load strategy from filesystem" {
        val tempFile = tempDir.resolve("custom.treeignore").toFile().apply {
            writeText("node_modules/\ndist/")
        }

        val lines = StrategyLoader.loadStrategyFile(
            "custom",
            Strategy.IGNORE,
            fromDirectory = tempDir.toFile()
        )
        lines shouldBe listOf("node_modules/", "dist/")

        tempFile.delete()
    }

    "should throw if require=true and nothing found" {
        shouldThrow<IOException> {
            StrategyLoader.loadStrategyFile(
                "nonexistent",
                Strategy.IGNORE,
                fromDirectory = tempDir.toFile(),
                require = true
            )
        }
    }

})