package com.glance.treetrunk.core.tree

import java.io.File

data class TreeNode(
    val file: File,
    val children: List<TreeNode> = emptyList()
)

object TextTreeBuilder {

    fun buildTree(root: File): TreeNode {
        val children = root.listFiles()
            ?.sortedWith(compareBy({ !it.isDirectory }, { it.name }))
            ?.map { buildTree(it) }
            ?: emptyList()

        return TreeNode(root, children)
    }

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