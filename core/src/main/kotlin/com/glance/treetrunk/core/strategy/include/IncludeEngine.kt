package com.glance.treetrunk.core.strategy.include

import com.glance.treetrunk.core.strategy.include.rule.IncludeRule
import java.io.File

/**
 * Engine for evaluating whether files should be included based on a set of [IncludeRule]s
 */
class IncludeEngine(private val rules: List<IncludeRule>) {

    /**
     * Determines whether the given file should be included based on the current include rules
     *
     * @param file the file to evaluate
     * @param relativePath the file's path relative to the root of the scan
     * @return true if the file should be included; false otherwise
     */
    fun shouldInclude(file: File, relativePath: String): Boolean {
        if (rules.isEmpty()) return true
        return rules.any { it.shouldInclude(file, relativePath) }
    }

    /**
     * Returns a new [IncludeEngine] instance with the current rules combined with additional rules
     *
     * Ensures rules remain unique by their rule key
     *
     * @param newRules additional include rules to add
     * @return a new [IncludeEngine] with the combined rules
     */
    fun addRules(newRules: List<IncludeRule>): IncludeEngine {
        val combined = (rules + newRules).distinctBy { it.ruleKey() }
        return IncludeEngine(combined)
    }

    /**
     * Returns whether the engine currently has any include rules defined
     */
    fun hasRules(): Boolean = rules.isNotEmpty()

}