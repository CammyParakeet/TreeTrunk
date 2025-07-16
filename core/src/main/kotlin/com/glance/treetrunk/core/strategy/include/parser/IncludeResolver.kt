package com.glance.treetrunk.core.strategy.include.parser

import com.glance.treetrunk.core.config.StrategyConfig
import com.glance.treetrunk.core.strategy.Strategy
import com.glance.treetrunk.core.strategy.StrategyFileParserRegistry
import com.glance.treetrunk.core.strategy.StrategyLoader
import com.glance.treetrunk.core.strategy.include.rule.GlobIncludeRule
import com.glance.treetrunk.core.strategy.include.rule.IncludeRule
import java.io.File

/**
 * Resolves [IncludeRule]s based on the provided [StrategyConfig] and a base directory
 *
 * Aggregates command-line provided patterns, presets, explicit files, and local include files within the directory
 */
object IncludeResolver {

    /**
     * Resolves and returns a complete list of [IncludeRule]s for the given base directory and strategy config
     *
     * @param baseDirectory the directory to scan for local include files
     * @param config the configured include strategies and sources
     * @return the complete list of applicable [IncludeRule]s
     */
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