package com.glance.treetrunk.core.test.ignore

import com.glance.treetrunk.core.strategy.ignore.IgnoreEngine
import com.glance.treetrunk.core.strategy.ignore.base.NameIgnoreRule
import com.glance.treetrunk.core.strategy.ignore.base.PatternIgnoreRule
import com.glance.treetrunk.core.strategy.ignore.base.RegexIgnoreRule
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class IgnoreEngineTest : StringSpec({
    "should respect combined ignore rules" {
        val rules = listOf(
            NameIgnoreRule("secret.txt"),
            PatternIgnoreRule("*.bak"),
            RegexIgnoreRule.fromGlob("temp/**")
        )
        val engine = IgnoreEngine(rules)

        engine.shouldIgnorePath("secret.txt") shouldBe true
        engine.shouldIgnorePath("secrets/secret.txt") shouldBe true

        engine.shouldIgnorePath("old.bak") shouldBe true
        engine.shouldIgnorePath("backups/old/old.bak") shouldBe true

        engine.shouldIgnorePath("temp/cache") shouldBe true
        engine.shouldIgnorePath("deep/path/temp/cache") shouldBe true

        engine.shouldIgnorePath("notes.txt") shouldBe false
        engine.shouldIgnorePath("deep/docs/notes.txt") shouldBe false
    }
})