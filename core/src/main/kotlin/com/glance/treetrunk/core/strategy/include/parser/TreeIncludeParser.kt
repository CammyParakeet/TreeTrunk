package com.glance.treetrunk.core.strategy.include.parser

import com.glance.treetrunk.core.strategy.StrategyFileParser
import com.glance.treetrunk.core.strategy.StrategyFileParser.Companion.parseLines
import com.glance.treetrunk.core.strategy.include.rule.IncludeRule
import com.glance.treetrunk.core.strategy.include.rule.GlobIncludeRule
import java.io.File

object TreeIncludeParser : StrategyFileParser<IncludeRule> {
    override val fileName: String = ".treeignore"
    override val fileNamePattern: Regex = """.*\.(treeignore|trunkignore)""".toRegex()

    override fun parse(file: File): List<IncludeRule> {
        return parseLines(file) { GlobIncludeRule(it) }
    }
}