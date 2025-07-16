package com.glance.treetrunk.core.strategy.ignore

import com.glance.treetrunk.core.strategy.Strategy
import com.glance.treetrunk.core.strategy.StrategyFileParserRegistry
import com.glance.treetrunk.core.strategy.StrategyLoader
import com.glance.treetrunk.core.strategy.ignore.rule.GlobIgnoreRule
import java.io.File

/**
 * Resolves ignore rules based on the provided [IgnoreOptions] and directory scanning
 */
object IgnoreResolver {

    fun resolve(baseDirectory: File, options: IgnoreOptions): List<IgnoreRule> {
        val rules = mutableListOf<IgnoreRule>()

        if (options.useDefaultIgnores) {
            rules += loadDefaults()
        }

        rules += options.customIgnoreNames.map { GlobIgnoreRule(it) }

        baseDirectory.listFiles()?.forEach { file ->
            StrategyFileParserRegistry.getParserFor<IgnoreRule>(file.name)?.let { parser ->
                rules += parser.parse(file)
            }
        }

        return rules
    }

    private fun loadDefaults(): List<IgnoreRule> {
        return StrategyLoader
            .loadStrategyFile("defaults", Strategy.IGNORE)
            ?.map { GlobIgnoreRule(it) }
            ?: emptyList()
    }

}