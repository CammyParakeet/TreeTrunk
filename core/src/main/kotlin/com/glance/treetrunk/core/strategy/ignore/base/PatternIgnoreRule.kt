package com.glance.treetrunk.core.strategy.ignore.base

import com.glance.treetrunk.core.strategy.ignore.IgnoreRule
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.PathMatcher
import java.nio.file.Paths

class PatternIgnoreRule(private val pattern: String) : IgnoreRule {

    private val matcher: PathMatcher = FileSystems.getDefault()
        .getPathMatcher("glob:**/$pattern")

    override fun shouldIgnore(file: File, relativePath: String): Boolean {
        val normalizedPath = relativePath.replace("/", File.separator)
        val path = Paths.get(normalizedPath)
        return matcher.matches(path)
    }

    override fun toString(): String {
        return "PatternIgnoreRule(pattern='$pattern')"
    }

}