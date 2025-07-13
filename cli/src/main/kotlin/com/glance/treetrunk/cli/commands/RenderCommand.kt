package com.glance.treetrunk.cli.commands

import com.glance.treetrunk.core.tree.Defaults
import com.glance.treetrunk.core.tree.Style
import com.glance.treetrunk.core.tree.StyleRegistry
import com.glance.treetrunk.core.tree.TextTreeBuilder
import com.glance.treetrunk.core.tree.model.RenderOptions
import picocli.CommandLine
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import java.io.File
import java.util.concurrent.Callable

/**
 * Main CLI Command to render a directory/project tree
 */
@CommandLine.Command(
    name = "render",
    description = ["Generates a text-based tree representation of your project structure"]
)
class RenderCommand : Callable<Int> {

    /**
     * The root directory to scan
     *
     * Defaults to the current directory if not specified
     */
    @Parameters(
        index = "0",
        description = ["Root directory to scan"],
        arity = "0..1"
    )
    var root: File = File(".")

    /**
     * Optional output file to export the rendered tree
     */
    @Option(
        names = ["--output", "--o"],
        description = ["Optional output file to write the tree to"]
    )
    var outputFile: File? = null

    @Option(
        names = ["--no-print"],
        description = ["Do not print to console (only write to file)"]
    )
    var noPrint: Boolean = false

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
        names = ["--max-depth", "--depth"],
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
        names = ["--max-children"],
        description = ["Maximum number of children per folder before collapsing (0 = no limit)"],
        defaultValue = "25"
    )
    var maxChildren: Int = Defaults.MAX_CHILDREN

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

    /**
     * If true, collapses chains of empty directories into dot-separated paths
     */
    @Option(
        names = ["--collapse-empty", "--collapse-packages"],
        description = ["Collapse empty directory chains (e.g. com/example/foo)"],
        defaultValue = "true"
    )
    var collapseEmpty: Boolean = true

    /**
     * Executes the command and renders the tree using the specified options
     */
    override fun call(): Int {
        if (!root.exists() || !root.isDirectory) {
            System.err.println("Invalid directory: ${root.absolutePath}")
            return 1
        }

        val options = RenderOptions(
            root,
            outputFile,
            style,
            customStyle,
            maxDepth,
            maxChildren,
            smartExpand,
            depthForgiveness,
            childForgiveness,
            collapseEmpty
        )

        val tree = TextTreeBuilder.buildTree(options, root)

        val symbols = customStyle?.let {
            StyleRegistry.get(it) ?: run {
                System.err.println("Custom style '$it' is not recognized")
                return 1
            }
        } ?: style.symbols

        val output = buildString {
            appendLine(root.name + "/")
            append(TextTreeBuilder.renderTree(tree, symbols = symbols))
        }

        val shouldPrintToConsole = outputFile == null || !noPrint
        if (shouldPrintToConsole) println(output)

        outputFile?.let { file ->
            file.writeText(output)
            println("Tree exported to ${file.absolutePath}")
        }

        return 0
    }

}