package com.glance.treetrunk.core.strategy.include.parser

import com.glance.treetrunk.core.config.StrategyConfig
import com.glance.treetrunk.core.strategy.Strategy
import com.glance.treetrunk.core.strategy.StrategyFileParserRegistry
import com.glance.treetrunk.core.strategy.StrategyLoader
import com.glance.treetrunk.core.strategy.include.rule.GlobIncludeRule
import com.glance.treetrunk.core.strategy.include.rule.IncludeRule
import java.io.File

/**
 * Resolves include rules based on the provided [StrategyConfig] and directory scanning
 */
object IncludeResolver {

    fun resolve(baseDirectory: File, config: StrategyConfig): List<IncludeRule> {
        val rules = mutableListOf<IncludeRule>()

        rules += config.includeList.map { GlobIncludeRule(it) }

        rules += config.includePresets.flatMap { preset ->
            StrategyLoader.loadStrategyFile(preset, Strategy.IGNORE)
        }

        rules += config.includeFiles.flatMap { path ->
            val file = File(path)
            if (file.exists() && file.isFile) {
                StrategyLoader.parseFile(file)
            } else emptyList()
        }

        if (config.useLocalIncludes) {
            baseDirectory.listFiles()?.forEach { file ->
                StrategyFileParserRegistry.getParserFor<IncludeRule>(file.name)?.let { parser ->
                    rules += parser.parse(file)
                }
            }
        }

        return rules
    }

}