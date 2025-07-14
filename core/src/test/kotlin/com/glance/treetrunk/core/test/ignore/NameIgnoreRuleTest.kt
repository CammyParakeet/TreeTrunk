package com.glance.treetrunk.core.test.ignore

import com.glance.treetrunk.core.strategy.ignore.base.NameIgnoreRule
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NameIgnoreRuleTest : StringSpec({
    "should match exact file name" {
        val rule = NameIgnoreRule("ignoreme.txt")
        rule.shouldIgnorePath("ignoreme.txt") shouldBe true
        rule.shouldIgnorePath("dontignore.txt") shouldBe false
    }
})