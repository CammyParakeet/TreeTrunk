package com.glance.treetrunk.core.config

import com.glance.treetrunk.core.strategy.include.InclusionMode

/**
 * Configuration for inclusion, exclusion, and ignore strategies that determine
 * which files and directories are scanned and rendered in the tree
 *
 * @property ignoreList direct list of ignore patterns (glob-like) to exclude items
 * @property includeList direct list of include patterns to explicitly include items
 * @property ignoreFiles paths to files containing ignore rules (e.g. `.gitignore`, `.treeignore`)
 * @property includeFiles paths to files containing include rules
 * @property ignorePresets names of predefined ignore rule sets to apply
 * @property includePresets names of predefined include rule sets to apply
 * @property useDefaultIgnores whether to apply built-in default ignore patterns (e.g. `.git`)
 * @property useLocalIgnores whether to apply the ignore rules from local ignore files in directories
 * @property useLocalIncludes whether to apply the include rules from local files
 * @property propagateIgnores whether to propagate ignore rules down to subdirectories
 * @property propagateIncludes whether to propagate include rules down to subdirectories
 * @property inclusionMode determines how include rules interact with ignore rules (soft override or hard filter)
 */
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
    val inclusionMode: InclusionMode = InclusionMode.OVERRIDE_IGNORE
)