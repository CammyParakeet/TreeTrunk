package com.glance.treetrunk.core.strategy.include

import com.glance.treetrunk.core.strategy.include.rule.IncludeRule
import java.io.File

class IncludeEngine(private val rules: List<IncludeRule>) {

    fun shouldInclude(file: File, relativePath: String): Boolean {
        if (rules.isEmpty()) return true
        return rules.any { it.shouldInclude(file, relativePath) }
    }

    fun addRules(newRules: List<IncludeRule>): IncludeEngine {
        val combined = (rules + newRules).distinctBy { it.ruleKey() }
        return IncludeEngine(combined)
    }

    fun hasRules(): Boolean = rules.isNotEmpty()

}