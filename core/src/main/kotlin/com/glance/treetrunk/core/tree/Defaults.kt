package com.glance.treetrunk.core.tree

import com.glance.treetrunk.core.strategy.DepthMode
import com.glance.treetrunk.core.strategy.StrategyFileParserRegistry
import com.glance.treetrunk.core.strategy.ignore.IgnoreRule
import com.glance.treetrunk.core.strategy.ignore.parser.GitIgnoreParser
import com.glance.treetrunk.core.strategy.ignore.parser.TreeIgnoreParser

/**
 * Global default values used throughout tree rendering
 */
object Defaults {
    const val MAX_DEPTH = 10
    const val MAX_CHILDREN = 25
    const val DEPTH_FORGIVENESS_THRESHOLD = 4
    const val CHILD_FORGIVENESS_THRESHOLD = 4
    val DEPTH_MODE = DepthMode.ANY_DEPTH

    fun registerDefaultStrategies() {
        StrategyFileParserRegistry.registerParser(IgnoreRule::class.java, GitIgnoreParser)
        StrategyFileParserRegistry.registerParser(IgnoreRule::class.java, TreeIgnoreParser)
        // TODO: includes?
    }

}