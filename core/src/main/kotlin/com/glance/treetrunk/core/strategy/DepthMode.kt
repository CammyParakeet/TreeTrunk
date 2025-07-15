package com.glance.treetrunk.core.strategy

/**
 * Controls how depth is evaluated in glob pattern matching
 */
enum class DepthMode {
    ROOT_ONLY,
    ANY_DEPTH,
    EXACT_PATH
}