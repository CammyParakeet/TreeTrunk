package com.glance.treetrunk.cli.commands.render

import com.glance.treetrunk.core.config.*
import com.glance.treetrunk.core.tree.StyleRegistry
import com.glance.treetrunk.core.tree.TreeBuilder
import picocli.CommandLine
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

    @CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1")
    var renderOptions: RenderOptions? = null

    @CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1")
    var strategyOptions: StrategyOptions? = null

    @CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1")
    var advancedOptions: AdvancedOptions? = null

    @CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1")
    var outputOptions: OutputOptions? = null

    /**
     * Executes the command and renders the tree using the specified options
     */
    override fun call(): Int {
        if (!root.exists() || !root.isDirectory) {
            System.err.println("Invalid directory: ${root.absolutePath}")
            return 1
        }

        val config = buildConfiguration(root)
        return executeRender(config)
    }

    private fun buildConfiguration(root: File): AppConfiguration {
      return AppConfiguration.builder()
          .root(root)
          .renderConfig(renderOptions?.config() ?: RenderConfig())
          .strategyConfig(strategyOptions?.config() ?: StrategyConfig())
          .advancedConfig(advancedOptions?.config() ?: AdvancedConfig())
          .build()
    }

    private fun executeRender(config: AppConfiguration): Int {
        val tree = TreeBuilder.buildTree(config)

        val symbols = config.renderConfig.customStyle?.let {
            StyleRegistry.get(it) ?: run {
                System.err.println("Custom style '${it}' is not recognized")
                return 1
            }
        } ?: config.renderConfig.style.symbols

        val output = buildString {
            appendLine(root.name + "/")
            append(TreeBuilder.renderTree(tree, symbols = symbols))
        }

        if (outputOptions?.shouldPrintToConsole() == false) {
            println(output)
        }

        outputOptions?.outputFile?.let {
            it.writeText(output)
            println("Tree exported to '${it.absolutePath}'")
        }

        return 0
    }

}