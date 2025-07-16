package com.glance.treetrunk.core.strategy.include.base

import com.glance.treetrunk.core.strategy.DepthMode
import com.glance.treetrunk.core.strategy.include.IncludeRule
import com.glance.treetrunk.core.strategy.pattern.GlobPattern
import com.glance.treetrunk.core.tree.Defaults
import java.io.File

/**
 * An [IncludeRule] backed by a glob pattern
 */
class GlobIncludeRule(
    private val glob: String,
    private val depthMode: DepthMode = Defaults.DEPTH_MODE
) : IncludeRule {

    private val pattern = GlobPattern(glob, depthMode)

    override fun matches(file: File, relativePath: String): Boolean {
        return pattern.matches(relativePath)
    }

    override fun ruleKey(): String {
        return "IncludeGlob:$glob:Depth:$depthMode"
    }

    override fun toString(): String {
        return "Glob($glob => ${pattern.getPattern()})"
    }

}