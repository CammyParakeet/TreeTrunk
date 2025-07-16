package com.glance.treetrunk.core.strategy.ignore.rule

import com.glance.treetrunk.core.strategy.StrategyRule
import java.io.File

/**
 * Contract for all ignore rule implementations
 */
interface IgnoreRule : StrategyRule {

    /**
     * Evaluates whether a file should be ignored
     */
    fun shouldIgnore(file: File, relativePath: String): Boolean = matches(file, relativePath)

    /**
     * Utility method to test ignoring purely based on a path string
     */
    fun shouldIgnorePath(relativePath: String): Boolean = matchesPath(relativePath)

}