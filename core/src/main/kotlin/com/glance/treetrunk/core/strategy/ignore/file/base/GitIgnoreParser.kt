package com.glance.treetrunk.core.strategy.ignore.file.base

import com.glance.treetrunk.core.strategy.ignore.IgnoreRule
import com.glance.treetrunk.core.strategy.ignore.base.GlobIgnoreRule
import com.glance.treetrunk.core.strategy.ignore.file.IgnoreFileParser
import java.io.File

object GitIgnoreParser : IgnoreFileParser {
    override val fileName: String = ".gitignore"
    override val fileNamePattern: Regex? = null

    override fun parse(file: File): List<IgnoreRule> {
        return file.readLines()
            .map { it.trim() }
            .filter { it.isNotBlank() && !it.startsWith("#") }
            .map { GlobIgnoreRule(it) }
    }
}