package com.glance.treetrunk.cli.commands.render

import com.glance.treetrunk.core.config.AdvancedConfig
import com.glance.treetrunk.core.tree.Defaults
import picocli.CommandLine.Option

/**
 * Advanced/experimental options
 */
class AdvancedOptions {

    /**
     * If true, allows exceeding max limits slightly when only a few entries are hidden
     *
     * Helps preserve readability when limits are nearly met
     */
    @Option(
        names = ["--forgive", "--smart-expand"],
        description = ["Attempt to render beyond limits if only a few entries are hidden"],
        defaultValue = "true"
    )
    var smartExpand: Boolean = true

    /**
     * Threshold for how many hidden entries can be forgiven at max depth
     *
     * Only used if smart expansion is enabled
     */
    @Option(
        names = ["--depth-forgiveness"],
        description = ["Override default render forgiveness at max depths"],
        defaultValue = "4"
    )
    var depthForgiveness: Int = Defaults.DEPTH_FORGIVENESS_THRESHOLD

    /**
     * Threshold for how many hidden children can be forgiven per directory
     *
     * Only used if smart expansion is enabled
     */
    @Option(
        names = ["--child-forgiveness"],
        description = ["Override default render forgiveness at max children"],
        defaultValue = "6"
    )
    var childForgiveness: Int = Defaults.CHILD_FORGIVENESS_THRESHOLD

    fun config(): AdvancedConfig {
        return AdvancedConfig(
            smartExpand,
            depthForgiveness,
            childForgiveness
        )
    }

}