package com.glance.treetrunk.core

import java.io.File

data class TreeNode(
    val file: File,
    val children: List<TreeNode> = emptyList()
)

object TreeBuilder {

    fun buildTree(root: File): TreeNode {
        val children = root.listFiles()
            ?.sortedWith(compareBy({ !it.isDirectory }, { it.name }))
            ?.map { buildTree(it) }
            ?: emptyList()

        return TreeNode(root, children)
    }

    fun renderTree(node: TreeNode, prefix: String = "", isLast: Boolean = true): String {
        val name = node.file.name + if (node.file.isDirectory) "/" else ""
        val builder = StringBuilder()
        builder.append(prefix)
        builder.append(if (isLast) TreeSymbols.LAST_BRANCH else TreeSymbols.BRANCH)
        builder.append(name).append("\n")

        val newPrefix = prefix + if (isLast) TreeSymbols.INDENT else TreeSymbols.VERTICAL
        node.children.forEachIndexed { i, childNode ->
            builder.append(renderTree(childNode, newPrefix, i == node.children.lastIndex))
        }

        return builder.toString()
    }

}