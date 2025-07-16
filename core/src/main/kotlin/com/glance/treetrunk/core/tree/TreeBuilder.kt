package com.glance.treetrunk.core.tree

import com.glance.treetrunk.core.config.AdvancedConfig
import com.glance.treetrunk.core.config.AppConfiguration
import com.glance.treetrunk.core.strategy.StrategyFileParserRegistry
import com.glance.treetrunk.core.strategy.ignore.IgnoreEngine
import com.glance.treetrunk.core.strategy.ignore.parser.IgnoreResolver
import com.glance.treetrunk.core.strategy.ignore.rule.IgnoreRule
import com.glance.treetrunk.core.strategy.include.IncludeEngine
import com.glance.treetrunk.core.strategy.include.InclusionMode
import com.glance.treetrunk.core.strategy.include.parser.IncludeResolver
import com.glance.treetrunk.core.strategy.include.rule.IncludeRule
import com.glance.treetrunk.core.tree.model.TreeNode
import java.io.File

/**
 * Responsible for building a recursive directory tree and rendering it into a defined structure
 */
object TreeBuilder {

    /**
     * Builds the tree structure starting from the provided root directory
     *
     * @param config TreeTrunk application config
     * @return A TreeNode representing the root and all its children
     *
     * TODO: Add support for, hidden files, symlink handling, and more
     */
    fun buildTree(
        config: AppConfiguration,
        currentDepth: Int = 0,
    ): TreeNode {
        val rootDir = config.root

        val ignoreRules = IgnoreResolver.resolve(rootDir, config.strategyConfig)
        val includeRules = IncludeResolver.resolve(rootDir, config.strategyConfig)

        val ignoreEngine = IgnoreEngine(ignoreRules)
        val includeEngine = IncludeEngine(includeRules)

        return buildRecursive(
            rootDir,
            config,
            currentDepth,
            "",
            ignoreEngine = ignoreEngine,
            includeEngine = includeEngine,
        ) ?: TreeNode(rootDir, emptyList())
    }

    /**
     * Recursively builds a tree node for the provided file, respecting configured rules
     * and rendering constraints
     *
     * @return a [TreeNode] or null if the file should be excluded
     */
    private fun buildRecursive(
        file: File,
        config: AppConfiguration,
        currentDepth: Int,
        relativePath: String,
        ignoreEngine: IgnoreEngine? = null,
        includeEngine: IncludeEngine? = null,
    ): TreeNode? {
        val ignored = (ignoreEngine?.shouldIgnore(file, relativePath) == true)
        val hasIncludes = includeEngine?.hasRules() == true
        val included = (includeEngine?.shouldInclude(file, relativePath) == true)

        when (config.strategyConfig.inclusionMode) {
            InclusionMode.OVERRIDE_IGNORE -> {
                if (ignored && (!hasIncludes || !included)) return null
            }
            InclusionMode.FILTER -> {
                if (hasIncludes && !included) return null
                if (ignored) return null
            }
        }

        var currentIgnoreEngine = ignoreEngine
        var currentIncludeEngine = includeEngine

        if (file.isDirectory) {
            if (config.strategyConfig.useLocalIgnores) {
                val localIgnoreRules = file.listFiles()?.flatMap { child ->
                    StrategyFileParserRegistry.getParserFor<IgnoreRule>(child.name)?.parse(child) ?: emptyList()
                }.orEmpty()

                if (localIgnoreRules.isNotEmpty()) {
                    currentIgnoreEngine = if (config.strategyConfig.propagateIgnores) {
                        ignoreEngine?.addRules(localIgnoreRules)
                    } else {
                        IgnoreEngine(localIgnoreRules)
                    }
                }
            }

            if (config.strategyConfig.useLocalIncludes) {
                val localIncludeRules = file.listFiles()?.flatMap { child ->
                    StrategyFileParserRegistry.getParserFor<IncludeRule>(child.name)?.parse(child) ?: emptyList()
                }.orEmpty()

                if (localIncludeRules.isNotEmpty()) {
                    currentIncludeEngine = if (config.strategyConfig.propagateIncludes) {
                        currentIncludeEngine?.addRules(localIncludeRules)
                    } else {
                        IncludeEngine(localIncludeRules)
                    }
                }
            }
        }

        val (effectiveFile, effectiveChildren) = if (config.renderConfig.collapseEmpty && file.isDirectory) {
            collapseEmptyDirs(file)
        } else {
            file to (file.listFiles()?.toList() ?: emptyList())
        }

        val files = effectiveChildren
            .sortedWith(compareBy({ !it.isDirectory }, { it.name }))

        val childNodes = files.mapNotNull { child ->
            buildRecursive(
                file = child,
                config = config,
                currentDepth = currentDepth + 1,
                relativePath = pathFor(child, relativePath),
                ignoreEngine = currentIgnoreEngine,
                includeEngine = currentIncludeEngine
            )
        }
        val childrenSize = childNodes.size

        if (config.renderConfig.maxChildren in 1..<childrenSize) {
            return handleMaxChildren(effectiveFile, childNodes, config)
        }

        if (config.renderConfig.maxDepth in 1..currentDepth) {
            return handleMaxDepth(effectiveFile, childNodes, config.advancedConfig)
        }

        return TreeNode(effectiveFile, childNodes)
    }

    /**
     * Constructs the relative path of a file within the tree structure
     *
     * Adds a trailing slash for directories
     */
    private fun pathFor(file: File, parentPath: String): String {
        val base = if (parentPath.isEmpty()) file.name else "$parentPath/${file.name}"
        return if (file.isDirectory) "$base/" else base
    }

    /**
     * Collapses chains of single-child directories into a synthetic path (e.g. com/example/foo -> com.example.foo)
     *
     * @return A pair of the synthetic File and the final directory's children
     */
    private fun collapseEmptyDirs(file: File): Pair<File, List<File>> {
        var current = file
        val chain = mutableListOf<String>()

        while (true) {
            val children = current.listFiles()?.filter { it.isDirectory || it.isFile } ?: break
            if (children.size != 1 || children.any { it.isFile }) break

            chain.add(current.name)
            current = children.first()
        }

        chain.add(current.name)
        return File(chain.joinToString(".")) to (current.listFiles()?.toList() ?: emptyList())
    }

    /**
     * Handles rendering behavior when the maximum number of children has been exceeded
     *
     * Depending on [AdvancedConfig.smartExpand] and forgiveness thresholds, it may either
     * display all children or insert a summary node indicating how many entries were omitted
     */
    private fun handleMaxChildren(
        root: File,
        children: List<TreeNode>,
        config: AppConfiguration
    ): TreeNode {
        val visible = children.take(config.renderConfig.maxChildren)
        val hidden = children.drop(config.renderConfig.maxChildren)

        val hiddenDirs = hidden.count { it.isDir }
        val hiddenFiles = hidden.size - hiddenDirs

        if (shouldForgive(hidden.size, config.advancedConfig.childForgiveness, config.advancedConfig.smartExpand)) {
            return TreeNode(root, children)
        }

        val summaryNode = TreeNode(
            File("... (${hidden.size} more entries: $hiddenDirs folders, $hiddenFiles files)")
        )

        return TreeNode(root, visible + summaryNode)
    }

    /**
     * Handles rendering behavior when the maximum depth of recursion has been reached
     *
     * If smart expansion is enabled and the remaining children are within the [AdvancedConfig.depthForgiveness],
     * the children will still be rendered. Otherwise, a summary node will be displayed instead
     */
    private fun handleMaxDepth(
        root: File,
        children: List<TreeNode>,
        config: AdvancedConfig
    ): TreeNode {
        if (children.isEmpty()) return TreeNode(root)

        val dirs = children.count { it.isDir }
        val files = children.size - dirs
        val onlyFiles = dirs == 0
        val underForgiveness = children.size <= config.depthForgiveness

        if (onlyFiles || (config.smartExpand && underForgiveness)) {
            return TreeNode(root, children)
        }

        val summary = "... ($dirs folders, $files files more)"
        return TreeNode(root, children = listOf(TreeNode(File(summary))))
    }

    // TODO move render logic out to decouple

    /**
     * Renders a tree structure to a formatted text string
     *
     * @param node Root TreeNode.
     * @param prefix Visual indentation passed recursively.
     * @param isLast Whether this is the last child in the current level.
     * @param symbols Symbol set used for rendering.
     * @return Rendered tree as a multiline string.
     *
     * TODO: Add options for line numbering, file size display, or annotations, other helpers based on presets
     */
    fun renderTree(
        node: TreeNode,
        prefix: String = "",
        isLast: Boolean = true,
        symbols: CliTreeSymbols = UnicodeSymbols
    ): String {
        val name = when {
            node.file.name.startsWith("...") -> node.file.name // todo anything custom
            node.isDir -> node.file.name + "/"
            else -> node.file.name
        }

        val builder = StringBuilder()
        builder.append(prefix)
        builder.append(if (isLast) symbols.lastBranch else symbols.branch)
        builder.append(name).append("\n")

        val newPrefix = prefix + if (isLast) symbols.indent else symbols.vertical
        node.children.forEachIndexed { i, childNode ->
            builder.append(renderTree(childNode, newPrefix, i == node.children.lastIndex, symbols))
        }

        return builder.toString()
    }

    /**
     * Determines if we should allow rendering past the threshold
     */
    private fun shouldForgive(hiddenCount: Int, threshold: Int, forgive: Boolean): Boolean {
        return forgive && hiddenCount <= threshold
    }

}