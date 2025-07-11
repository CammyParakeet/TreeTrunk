package com.glance.treetrunk.cli

import com.glance.treetrunk.core.tree.Style
import com.glance.treetrunk.core.tree.StyleRegistry
import com.glance.treetrunk.core.tree.TextTreeBuilder
import com.glance.treetrunk.core.tree.registerBuiltInStyles
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import java.io.File
import java.util.concurrent.Callable

@Command(
    name = "treetrunk",
    mixinStandardHelpOptions = true,
    version = ["TreeTrunk 0.1.0"],
    description = ["Generates a text-based tree representation of your project structure!"]
)
class Main : Callable<Int> {

    /**
     * The root directory to scan for building the tree
     */
    @Parameters(
        index = "0",
        description = ["Root directory to scan"],
        arity = "0..1"
    )
    var root: File? = null

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
        names = ["--style"],
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

    @Option(
        names = ["--list-styles"],
        description = ["Lists all available built-in and custom styles, then exits"]
    )
    var listStyles: Boolean = false

    override fun call(): Int {
        if (listStyles) {
            listStyles()
            return 0
        }

        val rootDir = root ?: kotlin.run {
            System.err.println("Missing required parameter: <root>")
            return 1
        }

        if (!rootDir.exists() || !rootDir.isDirectory) {
            System.err.println("Invalid directory: ${rootDir.absolutePath}")
            return 1
        }

        val tree = TextTreeBuilder.buildTree(rootDir)

        val symbols = customStyle?.let {
            StyleRegistry.get(it) ?: run {
                System.err.println("Custom style '$it' is not recognized")
                return 1
            }
        } ?: style.symbols

        val output = buildString {
            appendLine(rootDir.name + "/")
            append(TextTreeBuilder.renderTree(tree, symbols = symbols))
        }

        println(output)

        outputFile?.let { file ->
            file.writeText(output)
            println("Tree exported to ${file.absolutePath}")
        }

        return 0
    }

    private fun listStyles() {
        println("Available Tree Styles\n")

        println("Built-in styles (use with --style):")
        for (style in Style.entries) {
            println(" - ${style.name}")
        }

        val custom = StyleRegistry.all().keys - Style.entries.map { it.name }.toSet()
        if (custom.isNotEmpty()) {
            println("\nCustom styles (use with --custom-style):")
            for (name in custom) {
                println(" - $name")
            }
        } else {
            println("\nNo custom styles registered")
        }
    }

}

/**
 * CLI entrypoint for TreeTrunk
 */
fun main(args: Array<String>) {
    registerBuiltInStyles()
    CommandLine(Main()).execute(*args)
}