package com.glance.treetrunk.core.tree.render.text

import com.glance.treetrunk.core.tree.model.TreeNode
import com.glance.treetrunk.core.tree.render.TreeRenderer

object TextTreeRenderer : TreeRenderer<String, TextRenderOpts> {

    override fun render(node: TreeNode, options: TextRenderOpts): String {
        return renderNode(node, options = options)
    }

    /**
     * Renders a tree structure to a formatted text string
     *
     * @param node Root TreeNode
     * @param prefix Visual indentation passed recursively
     * @param isLast Whether this is the last child in the current level
     * @param options Options specific to this renderer
     * @return Rendered tree as a multiline string
     */
    private fun renderNode(
        node: TreeNode,
        prefix: String = "",
        isLast: Boolean = true,
        options: TextRenderOpts
    ): String {
        val symbols = options.symbols
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
            builder.append(
                renderNode(
                    childNode,
                    newPrefix,
                    i == node.children.lastIndex, options
                )
            )
        }

        return builder.toString()
    }

}