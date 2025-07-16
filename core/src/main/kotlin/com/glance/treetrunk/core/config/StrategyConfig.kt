package com.glance.treetrunk.core.config

data class StrategyConfig(
    val ignoreList: List<String> = listOf(),
    val includeList: List<String> = listOf(),
    val ignoreFiles: List<String> = listOf(),
    val includeFiles: List<String> = listOf(),
    val ignorePresets: List<String> = listOf(),
    val includePresets: List<String> = listOf(),
    val useDefaultIgnores: Boolean = true,
    val useLocalIgnores: Boolean = true,
    val useLocalIncludes: Boolean = true,
    val propagateIgnores: Boolean = true,
    val propagateIncludes: Boolean = true,
    val inclusionPriority: Boolean = true
)