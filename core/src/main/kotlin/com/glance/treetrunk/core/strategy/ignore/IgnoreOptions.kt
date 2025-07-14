package com.glance.treetrunk.core.strategy.ignore

import java.io.File

/**
 * Configuration options controlling how ignores are resolved and applied
 *
 * @param useDefaultIgnores Whether default ignore patterns should be loaded
 * @param customIgnoreNames A list of runtime supplied ignore names
 * @param ignoreFile An optional file of ignore names supplied at runtime
 * @param useLocalIgnores Whether to evaluate per-directory ignore files
 * @param propagateLocalIgnores Whether local ignore rules should propagate through subdirectories
 */
data class IgnoreOptions(
    val useDefaultIgnores: Boolean = true,
    val customIgnoreNames: List<String> = emptyList(),
    val ignoreFile: File? = null,
    val useLocalIgnores: Boolean = true,
    val propagateLocalIgnores: Boolean = true
)
