package com.glance.treetrunk.core.strategy.ignore.base

import com.glance.treetrunk.core.strategy.ignore.GlobToRegexConverter
import com.glance.treetrunk.core.strategy.ignore.IgnoreRule
import java.io.File

class RegexIgnoreRule(private val pattern: Regex) : IgnoreRule {

    override fun shouldIgnore(file: File, relativePath: String): Boolean {
        val normalizedPath = relativePath.replace("\\", "/")
        val result = pattern.matches(normalizedPath)
        println("RegexIgnoreRule: matching '$normalizedPath' against '$pattern' => $result")
        return result
    }

    override fun toString(): String {
        return "RegexIgnoreRule(pattern=$pattern)"
    }

    companion object {
        fun fromGlob(glob: String): RegexIgnoreRule {
            return RegexIgnoreRule(GlobToRegexConverter.convert(glob))
        }
    }

}