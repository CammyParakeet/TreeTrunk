package com.glance.treetrunk.core.config

import com.glance.treetrunk.core.tree.Defaults
import com.glance.treetrunk.core.tree.Style

data class RenderConfig(
    val style: Style = Style.UNICODE,
    val customStyle: String? = null,
    val maxDepth: Int = Defaults.MAX_DEPTH,
    val maxChildren: Int = Defaults.MAX_CHILDREN,
    val collapseEmpty: Boolean = true
)
