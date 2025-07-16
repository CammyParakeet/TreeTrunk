package com.glance.treetrunk.core.config

import com.glance.treetrunk.core.tree.Defaults

data class AdvancedConfig(
    val smartExpand: Boolean = true,
    val depthForgiveness: Int = Defaults.DEPTH_FORGIVENESS_THRESHOLD,
    val childForgiveness: Int = Defaults.CHILD_FORGIVENESS_THRESHOLD
)
