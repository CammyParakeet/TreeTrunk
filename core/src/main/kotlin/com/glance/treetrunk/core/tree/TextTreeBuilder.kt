package com.glance.treetrunk.core.tree

import com.glance.treetrunk.core.tree.model.RenderOptions
import com.glance.treetrunk.core.tree.model.TreeNode
import java.io.File

/**
 * Builds a recursive file tree and renders it in CLI-friendly formats
 */
object TextTreeBuilder {

    /**
     * Builds the tree structure starting from the provided root directory
     *
     * @param root The root directory to scan
     * @return A TreeNode representing the root and all its children
     *
     * TODO: Add support for preset & manual ignore lists, hidden files, symlink handling, and more
     * TODO: defaults from installed properties or cfg? Would need cli update too
     */
    fun buildTree(
        options: RenderOptions,
        root: File = options.root,
        currentDepth: Int = 0,
    ): TreeNode {
        val (effectiveRoot, effectiveChildren) = if (options.collapseEmpty && root.isDirectory) {
            collapseEmptyDirs(root)
        } else {
            root to (root.listFiles()?.toList() ?: emptyList())
        }

        val files = effectiveChildren
            .filterNot { shouldIgnore(it) }
            .sortedWith(compareBy({ !it.isDirectory }, { it.name }))

        if (options.maxChildren > 0 && files.size > options.maxChildren) {
            return handleMaxChildren(effectiveRoot, files, currentDepth, options)
        }

        if (currentDepth >= options.maxDepth) {
            return handleMaxDepth(effectiveRoot, files, currentDepth, options)
        }

        return TreeNode(effectiveRoot, getChildren(files, options, currentDepth))
    }

    /**
     * Returns immediate children as TreeNodes, increasing depth by 1
     */
    private fun getChildren(files: List<File>, options: RenderOptions, currentDepth: Int): List<TreeNode> {
        return files.map { buildTree(options, it, currentDepth + 1) }
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
        files: List<File>,
        currentDepth: Int,
        options: RenderOptions
    ): TreeNode {
        val visible = files.take(options.maxChildren)
        val hidden = files.drop(options.maxChildren)

        val (hiddenDirs, hiddenFiles) = hidden.partition { it.isDirectory }

        if (shouldForgive(hidden.size, options.childForgiveness, options.smartExpand)) {
            return TreeNode(root, getChildren(files, options, currentDepth))
        }

        val visibleChildren = getChildren(visible, options, currentDepth)

        val summaryNode = TreeNode(
            File("... (${hidden.size} more entries: ${hiddenDirs.size} folders, ${hiddenFiles.size} files)")
        )

        return TreeNode(root, visibleChildren + summaryNode)
    }

    /**
     * Handles logic when maxDepth threshold is reached
     */
    private fun handleMaxDepth(
        root: File,
        files: List<File>,
        currentDepth: Int,
        options: RenderOptions
    ): TreeNode {
        if (files.isEmpty()) return TreeNode(root)

        val (dirs, regularFiles) = files.partition { it.isDirectory }
        val onlyFiles = dirs.isEmpty()
        val underForgiveness = files.size <= options.depthForgiveness

        if (onlyFiles || (options.smartExpand && underForgiveness)) {
            return TreeNode(root, getChildren(files, options, currentDepth))
        }

        val summary = "... (${dirs.size} folders, ${regularFiles.size} files more)"
        return TreeNode(root, children = listOf(TreeNode(File(summary))))
    }


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

    /**
     * Returns true if the given file should be skipped from rendering
     * TODO: Expand later to use ignore files or patterns
     */
    private fun shouldIgnore(file: File): Boolean {
        return file.name == ".git"
    }

}