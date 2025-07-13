package com.glance.treetrunk.core.strategy.ignore.base

import com.glance.treetrunk.core.strategy.ignore.IgnoreRule
import java.io.File

class NameIgnoreRule(private val name: String): IgnoreRule {
    override fun shouldIgnore(file: File, relativePath: String): Boolean {
        return file.name == name
    }
}