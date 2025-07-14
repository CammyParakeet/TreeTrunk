package com.glance.treetrunk.core.test.ignore

import com.glance.treetrunk.core.strategy.ignore.base.RegexIgnoreRule
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RegexIgnoreRuleTest: StringSpec({
    "should match tmp files via glob" {
        val rule = RegexIgnoreRule.fromGlob("*.tmp")
        rule.shouldIgnorePath("file.tmp") shouldBe true
        rule.shouldIgnorePath("notes.txt") shouldBe false
        rule.shouldIgnorePath("deep/path/file.tmp") shouldBe true
    }

    "should match build directories anywhere via glob" {
        val rule = RegexIgnoreRule.fromGlob("**/build/**")
        rule.shouldIgnorePath("build") shouldBe true
        rule.shouldIgnorePath("nested/build") shouldBe true
        rule.shouldIgnorePath("nested/deeper/build/temp.txt") shouldBe true
        rule.shouldIgnorePath("nested/mybuild") shouldBe false
    }
})