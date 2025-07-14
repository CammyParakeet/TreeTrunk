package com.glance.treetrunk.core.strategy.ignore

import java.io.File

/**
 * Responsible for evaluating whether files or paths should be ignored
 * based on a list of [IgnoreRule]s
 */
class IgnoreEngine(private val rules: List<IgnoreRule>) {

    /**
     * Determines if a file should be ignored based on all configured rules
     */
    fun shouldIgnore(file: File, relativePath: String): Boolean {
        return rules.any { it.shouldIgnore(file, relativePath) }
    }

    /**
     * Path only usage
     */
    fun shouldIgnorePath(path: String) = shouldIgnore(File(path), path)

}