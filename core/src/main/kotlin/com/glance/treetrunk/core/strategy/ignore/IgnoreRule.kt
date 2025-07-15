package com.glance.treetrunk.core.strategy.ignore

import java.io.File

/**
 * Contract for all ignore rule implementations
 */
interface IgnoreRule {

    /**
     * Evaluates whether a file should be ignored
     */
    fun shouldIgnore(file: File, relativePath: String): Boolean

    /**
     * Utility method to test ignoring purely based on a path string
     */
    fun shouldIgnorePath(relativePath: String): Boolean {
        return shouldIgnore(File(relativePath), relativePath)
    }

    /**
     * Unique key representing this rule, for comparisons
     */
    fun ruleKey(): String

}