package com.glance.treetrunk.core.tree

import java.io.File

/**
 * Represents a file or folder in the tree
 *
 * @property file The actual file or directory
 * @property children List of child TreeNodes (only populated for directories)
 */
data class TreeNode(
    val file: File,
    val children: List<TreeNode> = emptyList()
)
