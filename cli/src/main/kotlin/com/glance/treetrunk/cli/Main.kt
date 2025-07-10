package com.glance.treetrunk.cli

import com.glance.treetrunk.core.tree.Style
import com.glance.treetrunk.core.tree.TextTreeBuilder
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
    @Parameters(index = "0", description = ["Root directory to scan"])
    lateinit var root: File

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

    override fun call(): Int {
        if (!root.exists() || !root.isDirectory) {
            System.err.println("Invalid directory: ${root.absolutePath}")
            return 1
        }

        val tree = TextTreeBuilder.buildTree(root)
        val output = buildString {
            appendLine(root.name + "/")
            append(TextTreeBuilder.renderTree(tree, symbols = style.symbols))
        }

        println(output)

        outputFile?.let { file ->
            file.writeText(output)
            println("Tree exported to ${file.absolutePath}")
        }

        return 0
    }
}

/**
 * CLI entrypoint for TreeTrunk
 */
fun main(args: Array<String>) {
    CommandLine(Main()).execute(*args)
}