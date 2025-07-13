package com.glance.treetrunk.core.strategy.ignore.base

import com.glance.treetrunk.core.strategy.ignore.IgnoreRule
import java.io.File

class GitIgnoreRule(private val ignoredNames: List<String>): IgnoreRule {
    override fun shouldIgnore(file: File, relativePath: String): Boolean {
        return ignoredNames.any { file.name == it }
    }

    // todo better parsing
    companion object {
        fun fromFile(file: File): GitIgnoreRule? {
            if (!file.exists() || !file.isFile) return null
            val ignoredNames = file.readLines()
                .map { it.trim() }
                .filter { it.isNotBlank() && !it.startsWith("#") }
            return GitIgnoreRule(ignoredNames)
        }
    }
}