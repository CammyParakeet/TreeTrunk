package com.glance.treetrunk.core.strategy.ignore

import java.io.File

class IgnoreEngine(private val rules: List<IgnoreRule>) {

    fun shouldIgnore(file: File, relativePath: String): Boolean {
        return rules.any { it.shouldIgnore(file, relativePath) }
    }

    fun shouldIgnorePath(path: String) = shouldIgnore(File(path), path)

}