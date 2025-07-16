package com.glance.treetrunk.core.config

import com.glance.treetrunk.core.tree.Defaults

/**
 * Configuration for advanced rendering behaviors that influence
 * how the tree traversal adapts to constraints like depth or child limits
 *
 * @property smartExpand whether to intelligently expand nodes despite limits if few children are hidden
 * @property depthForgiveness number of additional depth levels allowed when near the max depth limit
 * @property childForgiveness number of extra children allowed when the max child limit is nearly hit
 */
data class AdvancedConfig(
    val smartExpand: Boolean = true,
    val depthForgiveness: Int = Defaults.DEPTH_FORGIVENESS_THRESHOLD,
    val childForgiveness: Int = Defaults.CHILD_FORGIVENESS_THRESHOLD
)
