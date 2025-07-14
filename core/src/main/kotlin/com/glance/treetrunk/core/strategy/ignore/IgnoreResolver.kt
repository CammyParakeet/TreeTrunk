package com.glance.treetrunk.core.strategy.ignore

import com.glance.treetrunk.core.strategy.ignore.base.GitIgnoreRule

/**
 * Resolves ignore rules based on [IgnoreOptions]
 */
object IgnoreResolver {

    //private val defaultIgnores = loadDefaults()

//    fun resolve(options: IgnoreOptions): List<IgnoreRule> {
//        val rules = mutableListOf<IgnoreRule>()
//
//        if (options.useDefaultIgnores) {
//            rules += defaultIgnores.map((::PatternIgnoreRule))
//        }
//
//        rules += options.customIgnoreNames.map(::PatternIgnoreRule)
//
//        options.ignoreFile
//            ?.takeIf { it.exists() }
//            ?.let { GitIgnoreRule.fromFile(it) }
//            ?.let { rules += it }
//
//        return rules
//    }
//
//    private fun loadDefaults(): List<String> {
//        val stream = javaClass.getResourceAsStream("/defaults.trunkignore")
//            ?: return emptyList()
//
//        return stream.bufferedReader().useLines { lines ->
//            lines.map { it.trim() }
//                .filter { it.isNotBlank() && !it.startsWith("#") }
//                .toList()
//        }
//    }

}