package com.glance.treetrunk.core.strategy.ignore.parser

import com.glance.treetrunk.core.strategy.StrategyFileParser
import com.glance.treetrunk.core.strategy.StrategyFileParser.Companion.parseLines
import com.glance.treetrunk.core.strategy.ignore.rule.IgnoreRule
import com.glance.treetrunk.core.strategy.ignore.rule.GlobIgnoreRule
import java.io.File

/**
 * Strategy file parser for '.gitignore' files
 *
 * Parses each line of the '.gitignore' file into a [GlobIgnoreRule]
 */
object GitIgnoreParser : StrategyFileParser<IgnoreRule> {
    override val fileName: String = ".gitignore"
    override val fileNamePattern: Regex? = null

    override fun parse(file: File): List<IgnoreRule> {
        return parseLines(file) { GlobIgnoreRule(it) }
    }
}