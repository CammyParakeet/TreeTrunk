package com.glance.treetrunk.core.strategy.ignore.parser

import com.glance.treetrunk.core.config.StrategyConfig
import com.glance.treetrunk.core.strategy.Strategy
import com.glance.treetrunk.core.strategy.StrategyFileParserRegistry
import com.glance.treetrunk.core.strategy.StrategyLoader
import com.glance.treetrunk.core.strategy.ignore.rule.GlobIgnoreRule
import com.glance.treetrunk.core.strategy.ignore.rule.IgnoreRule
import java.io.File

/**
 * Resolves ignore rules based on the provided [StrategyConfig] and directory scanning
 */
object IgnoreResolver {

    /**
     * Resolves and returns a complete list of [IgnoreRule]s for the given base directory and strategy config
     *
     * @param baseDirectory the directory to scan for local ignore files
     * @param config the configured ignore strategies and sources
     * @return the complete list of applicable [IgnoreRule]s
     */
    fun resolve(baseDirectory: File, config: StrategyConfig): List<IgnoreRule> {
        val rules = mutableListOf<IgnoreRule>()

        if (config.useDefaultIgnores) {
            rules += loadDefaults()
        }

        rules += config.ignoreList.map { GlobIgnoreRule(it) }

        rules += config.ignorePresets.flatMap { preset ->
            StrategyLoader.loadStrategyFile<IgnoreRule>(preset, Strategy.IGNORE)
        }

        rules += config.ignoreFiles.flatMap { path ->
            val file = File(path)
            if (file.exists() && file.isFile) {
                StrategyLoader.parseFile(file)
            } else emptyList()
        }

        if (config.useLocalIgnores) {
            baseDirectory.listFiles()?.forEach { file ->
                StrategyFileParserRegistry.getParserFor<IgnoreRule>(file.name)?.let { parser ->
                    rules += parser.parse(file)
                }
            }
        }

        return rules
    }

    /** Loads the default ignore rules from the 'defaults' preset */
    private fun loadDefaults(): List<IgnoreRule> {
        return StrategyLoader
            .loadStrategyFile<IgnoreRule>("defaults", Strategy.IGNORE)
    }

}