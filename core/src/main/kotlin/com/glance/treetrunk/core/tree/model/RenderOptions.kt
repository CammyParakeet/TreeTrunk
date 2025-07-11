package com.glance.treetrunk.core.tree.model

import com.glance.treetrunk.core.tree.Defaults
import com.glance.treetrunk.core.tree.Style
import java.io.File

/**
 * Configuration options for building and rendering a file tree
 */
data class RenderOptions(
    val root: File = File("."),
    val outputFile: File? = null,
    val style: Style = Style.UNICODE,
    val customStyle: String? = null,
    val maxDepth: Int = Defaults.MAX_DEPTH,
    val maxChildren: Int = Defaults.MAX_CHILDREN,
    val smartExpand: Boolean = true,
    val depthForgiveness: Int = Defaults.DEPTH_FORGIVENESS_THRESHOLD,
    val childForgiveness: Int = Defaults.CHILD_FORGIVENESS_THRESHOLD,
    val collapseEmpty: Boolean = true
)
