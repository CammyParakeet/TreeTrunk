package com.glance.treetrunk.core.tree

import com.glance.treetrunk.core.strategy.StrategyFileParserRegistry
import com.glance.treetrunk.core.strategy.ignore.IgnoreEngine
import com.glance.treetrunk.core.strategy.ignore.IgnoreResolver
import com.glance.treetrunk.core.strategy.ignore.IgnoreRule
import com.glance.treetrunk.core.tree.model.RenderOptions
import com.glance.treetrunk.core.tree.model.TreeNode
import java.io.File

/**
 * Builds a recursive file tree and renders it in CLI-friendly formats
 */
object TreeBuilder {

    /**
     * Builds the tree structure starting from the provided root directory
     *
     * @param root The root directory to scan
     * @return A TreeNode representing the root and all its children
     *
     * TODO: Add support for preset, hidden files, symlink handling, and more
     * TODO: defaults from installed properties or cfg? Would need cli update too
     */
    fun buildTree(
        options: RenderOptions,
        root: File = options.root,
        currentDepth: Int = 0,
    ): TreeNode {
        val rules = IgnoreResolver.resolve(root, options.ignoreOptions)
        val ignoreEngine = IgnoreEngine(rules)

        return buildRecursive(
            root,
            ignoreEngine,
            options,
            currentDepth,
            ""
        ) ?: TreeNode(root, emptyList())
    }

    private fun buildRecursive(
        file: File,
        ignoreEngine: IgnoreEngine,
        options: RenderOptions,
        currentDepth: Int,
        relativePath: String
    ): TreeNode? {
        if (ignoreEngine.shouldIgnore(file, relativePath)) return null

        var currentEngine = ignoreEngine

        if (file.isDirectory && options.ignoreOptions.useLocalIgnores) {
            val localIgnoreRules = file.listFiles()?.flatMap { child ->
                StrategyFileParserRegistry.getParserFor<IgnoreRule>(child.name)?.parse(child) ?: emptyList()
            }.orEmpty()

            if (localIgnoreRules.isNotEmpty()) {
                currentEngine = if (options.ignoreOptions.propagateLocalIgnores) {
                    ignoreEngine.addRules(localIgnoreRules)
                } else {
                    IgnoreEngine(localIgnoreRules)
                }
            }
        }

        val (effectiveFile, effectiveChildren) = if (options.collapseEmpty && file.isDirectory) {
            collapseEmptyDirs(file)
        } else {
            file to (file.listFiles()?.toList() ?: emptyList())
        }

        val files = effectiveChildren
            .sortedWith(compareBy({ !it.isDirectory }, { it.name }))

        val childNodes = files.mapNotNull { child ->
            buildRecursive(
                file = child,
                ignoreEngine = currentEngine,
                options = options,
                currentDepth = currentDepth + 1,
                relativePath = pathFor(child, relativePath)
            )
        }
        val childrenSize = childNodes.size

        if (options.maxChildren in 1..<childrenSize) {
            return handleMaxChildren(effectiveFile, childNodes, options)
        }

        if (options.maxDepth in 1..currentDepth) {
            return handleMaxDepth(effectiveFile, childNodes, options)
        }

        return TreeNode(effectiveFile, childNodes)
    }

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
     * Handles logic when maxChildren threshold is exceeded
     */
    private fun handleMaxChildren(
        root: File,
        children: List<TreeNode>,
        options: RenderOptions
    ): TreeNode {
        val visible = children.take(options.maxChildren)
        val hidden = children.drop(options.maxChildren)

        val hiddenDirs = hidden.count { it.isDir }
        val hiddenFiles = hidden.size - hiddenDirs

        if (shouldForgive(hidden.size, options.childForgiveness, options.smartExpand)) {
            return TreeNode(root, children)
        }

        val summaryNode = TreeNode(
            File("... (${hidden.size} more entries: $hiddenDirs folders, $hiddenFiles files)")
        )

        return TreeNode(root, visible + summaryNode)
    }

    /**
     * Handles logic when maxDepth threshold is reached
     */
    private fun handleMaxDepth(
        root: File,
        children: List<TreeNode>,
        options: RenderOptions
    ): TreeNode {
        if (children.isEmpty()) return TreeNode(root)

        val dirs = children.count { it.isDir }
        val files = children.size - dirs
        val onlyFiles = dirs == 0
        val underForgiveness = children.size <= options.depthForgiveness

        if (onlyFiles || (options.smartExpand && underForgiveness)) {
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