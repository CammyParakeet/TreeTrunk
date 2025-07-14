package com.glance.treetrunk.core.strategy.ignore

import com.glance.treetrunk.core.strategy.ignore.base.GlobIgnoreRule
import com.glance.treetrunk.core.strategy.ignore.file.IgnoreFileParserRegistry
import com.glance.treetrunk.core.strategy.ignore.file.base.TreeIgnoreParser
import java.io.File

/**
 * Resolves ignore rules based on the provided [IgnoreOptions] and directory scanning
 */
object IgnoreResolver {

    fun resolve(baseDirectory: File, options: IgnoreOptions): List<IgnoreRule> {
        val rules = mutableListOf<IgnoreRule>()

        if (options.useDefaultIgnores) {
            rules += loadDefaults()
        }

        rules += options.customIgnoreNames.map { GlobIgnoreRule(it) }

        // Check for ignore files in the base dir - TODO: move this to the builder?
        baseDirectory.listFiles()?.forEach { file ->
            IgnoreFileParserRegistry.findParserFor(file.name)?.let { parser ->
                rules += parser.parse(file)
            }
        }

        return rules
    }

    private fun loadDefaults(): List<IgnoreRule> {
        val resource = javaClass.getResourceAsStream("/defaults.treeignore") ?: return emptyList()

        val tempFile = kotlin.io.path.createTempFile().toFile().apply {
            writeBytes(resource.readAllBytes())
            deleteOnExit()
        }

        return TreeIgnoreParser.parse(tempFile)
    }

}