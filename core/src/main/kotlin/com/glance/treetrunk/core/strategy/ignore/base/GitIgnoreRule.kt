package com.glance.treetrunk.core.strategy.ignore.base

import com.glance.treetrunk.core.strategy.ignore.IgnoreRule
import java.io.File

class GitIgnoreRule(private val ignoredNames: List<IgnoreRule>): IgnoreRule {

    override fun shouldIgnore(file: File, relativePath: String): Boolean {
        return ignoredNames.any { it.shouldIgnore(file, relativePath) }
    }

    override fun toString(): String {
        return "GitIgnoreRule(ignoredNames=$ignoredNames)"
    }


//    companion object {
//        fun fromFile(file: File): GitIgnoreRule? {
//            if (!file.exists() || !file.isFile) return null
//            val patterns = file.readLines()
//                .map { it.trim() }
//                .filter { it.isNotBlank() && !it.startsWith("#") }
//                .map { convertToRule(it) }
//            return GitIgnoreRule(patterns)
//        }
//
//        private fun convertToRule(pattern: String): IgnoreRule {
//            // TODO
//        }
//    }

}