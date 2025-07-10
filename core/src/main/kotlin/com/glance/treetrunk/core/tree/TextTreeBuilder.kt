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
     * TODO: Add support for preset & manual ignore lists, max depth, hidden files, symlink handling, and more
     */
    fun buildTree(root: File): TreeNode {
        val children = root.listFiles()
            ?.sortedWith(compareBy({ !it.isDirectory }, { it.name }))
            ?.map { buildTree(it) }
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
        val name = node.file.name + if (node.file.isDirectory) "/" else ""
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