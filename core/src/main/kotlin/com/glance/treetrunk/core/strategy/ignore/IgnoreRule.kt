package com.glance.treetrunk.core.strategy.ignore

import java.io.File

interface IgnoreRule {
    fun shouldIgnore(file: File, relativePath: String): Boolean

    fun shouldIgnorePath(relativePath: String): Boolean {
        return shouldIgnore(File(relativePath), relativePath)
    }
}