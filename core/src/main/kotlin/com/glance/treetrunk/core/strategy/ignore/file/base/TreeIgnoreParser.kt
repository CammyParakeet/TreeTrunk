package com.glance.treetrunk.core.strategy.ignore.file.base

import com.glance.treetrunk.core.strategy.ignore.IgnoreRule
import com.glance.treetrunk.core.strategy.ignore.base.GlobIgnoreRule
import com.glance.treetrunk.core.strategy.ignore.file.IgnoreFileParser
import java.io.File

object TreeIgnoreParser : IgnoreFileParser {
    override val fileName: String = ".treeignore"
    override val fileNamePattern: Regex = """.*\.(treeignore|trunkignore)""".toRegex()

    override fun parse(file: File): List<IgnoreRule> {
        // todo centralize this code block
        return file.readLines()
            .map { it.trim() }
            .filter { it.isNotBlank() && !it.startsWith("#") }
            .map { GlobIgnoreRule(it) }
    }
}