package com.glance.treetrunk.core.test.ignore

import com.glance.treetrunk.core.strategy.ignore.base.GitIgnoreRule
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class GitIgnoreRuleTest : StringSpec({
    "should parse gitignore and match files" {
        val fakeIgnore = kotlin.io.path.createTempFile().toFile()
        fakeIgnore.writeText("""
            *.log
            build/
            dist/
            out/
            *.tmp
            cache/
        """.trimIndent())

        val rule = GitIgnoreRule.fromFile(fakeIgnore)!!

        println("About to test Git Ignore from rule: $rule")

        // --- Wildcard file patterns ---
        rule.shouldIgnorePath("error.log") shouldBe true
        rule.shouldIgnorePath("logs/debug.log") shouldBe true
        rule.shouldIgnorePath("temp/cache.tmp") shouldBe true
        rule.shouldIgnorePath("deep/temp/deepcache.tmp") shouldBe true

        // --- Directory ignores ---
        rule.shouldIgnorePath("build") shouldBe true
        rule.shouldIgnorePath("build/output.txt") shouldBe true
        rule.shouldIgnorePath("deep/nested/build") shouldBe true
        rule.shouldIgnorePath("deep/nested/build/artifact.jar") shouldBe true

        rule.shouldIgnorePath("dist") shouldBe true
        rule.shouldIgnorePath("dist/assets/image.png") shouldBe true

        rule.shouldIgnorePath("cache") shouldBe true
        rule.shouldIgnorePath("nested/cache") shouldBe true
        rule.shouldIgnorePath("nested/cache/image.png") shouldBe true

        // --- Files and paths that should NOT be ignored ---
        rule.shouldIgnorePath("main.kt") shouldBe false
        rule.shouldIgnorePath("src/main.kt") shouldBe false
        rule.shouldIgnorePath("docs/readme.md") shouldBe false
        rule.shouldIgnorePath("build.gradle.kts") shouldBe false

        fakeIgnore.delete()
    }
})