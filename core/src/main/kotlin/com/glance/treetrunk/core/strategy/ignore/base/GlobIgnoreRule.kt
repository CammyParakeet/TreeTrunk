package com.glance.treetrunk.core.strategy.ignore.base

import com.glance.treetrunk.core.strategy.DepthMode
import com.glance.treetrunk.core.strategy.ignore.IgnoreRule
import com.glance.treetrunk.core.tree.Defaults
import java.io.File

/**
 * An [IgnoreRule] backed by a glob pattern
 */
class GlobIgnoreRule(
    private val glob: String,
    private val depthMode: DepthMode = Defaults.DEPTH_MODE
) : IgnoreRule {

    private val pattern = GlobPattern(glob, depthMode)

    override fun shouldIgnore(file: File, relativePath: String): Boolean {
        return pattern.matches(relativePath)
    }

    override fun ruleKey(): String {
        return "Glob:$glob:Depth:$depthMode"
    }

    override fun toString(): String {
        return "Glob($glob => ${pattern.getPattern()})"
    }


}