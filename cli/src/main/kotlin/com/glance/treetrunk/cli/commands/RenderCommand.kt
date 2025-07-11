package com.glance.treetrunk.cli.commands

import com.glance.treetrunk.core.tree.Style
import com.glance.treetrunk.core.tree.StyleRegistry
import com.glance.treetrunk.core.tree.TextTreeBuilder
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

    override fun call(): Int {
        if (!root.exists() || !root.isDirectory) {
            System.err.println("Invalid directory: ${root.absolutePath}")
            return 1
        }

        val tree = TextTreeBuilder.buildTree(root)

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

        println(output)

        outputFile?.let { file ->
            file.writeText(output)
            println("Tree exported to ${file.absolutePath}")
        }

        return 0
    }

}