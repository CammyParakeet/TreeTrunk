package com.glance.treetrunk.core.strategy

import java.io.File

/**
 * Contract for all strategy rules (Ignore, Include, etc.)
 */
interface StrategyRule {
    /**
     * Tests if the rule applies to the given file and path
     */
    fun matches(file: File, relativePath: String): Boolean

    /**
     * Tests if the rule applies to a given relative path alone
     */
    fun matchesPath(relativePath: String): Boolean {
        return matches(File(relativePath), relativePath)
    }

    /**
     * Unique key representing this rule instance
     */
    fun ruleKey(): String
}