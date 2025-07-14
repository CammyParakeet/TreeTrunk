package com.glance.treetrunk.core.test.ignore

import com.glance.treetrunk.core.strategy.ignore.base.PatternIgnoreRule
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PatternIgnoreRuleTest : StringSpec({
    "should match wildcard patterns" {
        val rule = PatternIgnoreRule("*.log")
        rule.shouldIgnorePath("error.log") shouldBe true
        rule.shouldIgnorePath("note.txt") shouldBe false
    }

    "should match recursive directory patterns" {
        val rule = PatternIgnoreRule("logs/**")
        rule.shouldIgnorePath("logs/debug.log") shouldBe true
        rule.shouldIgnorePath("logs/sub/error.log") shouldBe true
    }
})