package com.glance.treetrunk.core.strategy.ignore.parser

import com.glance.treetrunk.core.strategy.StrategyFileParser
import com.glance.treetrunk.core.strategy.StrategyFileParser.Companion.parseLines
import com.glance.treetrunk.core.strategy.ignore.rule.IgnoreRule
import com.glance.treetrunk.core.strategy.ignore.rule.GlobIgnoreRule
import java.io.File

/**
 * Strategy file parser for '.treeignore' and related custom ignore files
 *
 * Matches files ending in '.treeignore' or '.trunkignore' via regex
 */
object TreeIgnoreParser : StrategyFileParser<IgnoreRule> {
    override val fileName: String = ".treeignore"
    override val fileNamePattern: Regex = """.*\.(treeignore|trunkignore)""".toRegex()

    override fun parse(file: File): List<IgnoreRule> {
        return parseLines(file) { GlobIgnoreRule(it) }
    }
}