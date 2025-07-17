package com.glance.treetrunk.core.strategy.include.rule

import com.glance.treetrunk.core.strategy.StrategyRule
import java.io.File

/**
 * Contract for all include rule implementations
 */
interface IncludeRule : StrategyRule {

    /**
     * Evaluates whether a file should be included
     */
    fun shouldInclude(file: File, relativePath: String): Boolean = matches(file, relativePath)

    /**
     * Utility method to test including purely based on a path string
     */
    fun shouldIncludePath(relativePath: String): Boolean = matchesPath(relativePath)

}