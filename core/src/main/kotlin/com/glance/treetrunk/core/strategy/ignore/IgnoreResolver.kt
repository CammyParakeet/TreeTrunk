package com.glance.treetrunk.core.strategy.ignore

import com.glance.treetrunk.core.strategy.ignore.base.GitIgnoreRule
import com.glance.treetrunk.core.strategy.ignore.base.NameIgnoreRule

object IgnoreResolver {

    // TODO - install file for this
    private val defaultIgnores = listOf(
        ".git", ".idea", "node_modules", "build", "out", "dist", ".DS_Store"
    )

    fun resolve(options: IgnoreOptions): List<IgnoreRule> {
        val rules = mutableListOf<IgnoreRule>()

        if (options.useDefaultIgnores) {
            rules += defaultIgnores.map((::NameIgnoreRule))
        }

        rules += options.customIgnoreNames.map(::NameIgnoreRule)

        options.ignoreFile
            ?.takeIf { it.exists() }
            ?.let { GitIgnoreRule.fromFile(it) }
            ?.let { rules += it }

        return rules
    }

}