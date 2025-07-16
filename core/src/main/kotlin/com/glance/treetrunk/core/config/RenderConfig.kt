package com.glance.treetrunk.core.config

import com.glance.treetrunk.core.tree.Defaults
import com.glance.treetrunk.core.tree.Style

/**
 * Configuration for controlling the appearance and formatting of the rendered tree output
 *
 * @property style predefined rendering style, e.g. UNICODE or ASCII
 * @property customStyle optional custom style name to override the default style symbols
 * @property maxDepth maximum depth to render in the tree; directories deeper than this are excluded
 * @property maxChildren maximum number of children to render per directory
 * @property collapseEmpty whether to collapse empty directories into a single summarized entry
 */
data class RenderConfig(
    val style: Style = Style.UNICODE,
    val customStyle: String? = null,
    val maxDepth: Int = Defaults.MAX_DEPTH,
    val maxChildren: Int = Defaults.MAX_CHILDREN,
    val collapseEmpty: Boolean = true
)
