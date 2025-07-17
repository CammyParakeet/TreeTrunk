package com.glance.treetrunk.core.tree.render

import com.glance.treetrunk.core.tree.model.TreeNode

interface TreeRenderer<O, in OPTS> {
    fun render(node: TreeNode, options: OPTS): O
}