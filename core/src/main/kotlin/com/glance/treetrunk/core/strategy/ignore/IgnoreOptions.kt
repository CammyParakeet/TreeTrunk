package com.glance.treetrunk.core.strategy.ignore

import java.io.File

data class IgnoreOptions(
    val useDefaultIgnores: Boolean = true,
    val customIgnoreNames: List<String> = emptyList(),
    val ignoreFile: File? = null,
    val useLocalIgnores: Boolean = true,
    val propagateLocalIgnored: Boolean = true
)
