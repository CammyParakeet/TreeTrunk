package com.glance.treetrunk.cli.commands.render

import com.glance.treetrunk.core.config.RenderConfig
import com.glance.treetrunk.core.tree.Defaults
import com.glance.treetrunk.core.tree.Style
import picocli.CommandLine.Option

/**
 * Core rendering configuration options
 */
class RenderOptions {

    /**
     * Output rendering style
     */
    @Option(
        names = ["--style", "--s"],
        description = ["Tree rendering style: \${COMPLETION-CANDIDATES}"],
        defaultValue = "UNICODE"
    )
    var style: Style = Style.UNICODE

    /**
     * Override --style with a custom registered style
     */
    @Option(
        names = ["--custom-style"],
        description = ["Name of a dynamically registered style (overrides --style if provided)"]
    )
    var customStyle: String? = null

    /**
     * Maximum depth of directory traversal before truncating output
     *
     * Set higher to explore deeply nested structures
     */
    @Option(
        names = ["--max-depth", "--depth", "--d"],
        description = ["Maximum directory depth to render (default: 8)"],
        defaultValue = "10"
    )
    var maxDepth: Int = Defaults.MAX_DEPTH

    /**
     * Maximum number of children to render per directory before summarizing
     *
     * Use 0 to disable this limit entirely at your own discretion
     */
    @Option(
        names = ["--max-children", "--b"],
        description = ["Maximum number of children per folder before collapsing (0 = no limit)"],
        defaultValue = "25"
    )
    var maxChildren: Int = Defaults.MAX_CHILDREN

    /**
     * If true, collapses chains of empty directories into dot-separated paths
     */
    @Option(
        names = ["--collapse-empty", "--collapse-packages", "--c"],
        description = ["Collapse empty directory chains (e.g. com/example/foo)"],
        defaultValue = "true"
    )
    var collapseEmpty: Boolean = false

    fun config(): RenderConfig {
        return RenderConfig(
            style,
            customStyle,
            maxDepth,
            maxChildren,
            collapseEmpty
        )
    }

}