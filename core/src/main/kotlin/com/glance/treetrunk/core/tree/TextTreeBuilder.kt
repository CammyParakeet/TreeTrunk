package com.glance.treetrunk.core.tree

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
        root: File,
        currentDepth: Int = 0,
        maxDepth: Int = 8,
        maxChildren: Int = 25
    ): TreeNode {
        // todo sorting
        val files = root.listFiles() ?: emptyArray()
        val (dirs, regularFiles) = files.partition { it.isDirectory }

        if (maxChildren > 0 && files.size > maxChildren) {
            val visible = files.take(maxChildren)
            val hidden = files.drop(maxChildren)

            val visibleChildren = visible.map {
                buildTree(it, currentDepth + 1, maxDepth, maxChildren)
            }

            val summaryNode = TreeNode(
                File("... (${hidden.size} more entries: ${dirs.size} folders, ${regularFiles.size} files)")
            )

            return TreeNode(root, visibleChildren + summaryNode)
        }

        if (currentDepth >= maxDepth) {
            if (files.isEmpty()) {
                return TreeNode(root)
            }

            val summary = "... (${dirs.size} folders, ${regularFiles.size} files more)"
            return TreeNode(root, children = listOf(
                TreeNode(File(summary))
            ))
        }

        val children = root.listFiles()
            ?.sortedWith(compareBy({ !it.isDirectory }, { it.name }))
            ?.map { buildTree(it, currentDepth + 1, maxDepth) }
            ?: emptyList()

        return TreeNode(root, children)
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
            node.file.isDirectory -> node.file.name + "/"
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

}