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

    /**
     * Options that control rendering output such as styling and formatting
     */
    @CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1")
    var renderOptions: RenderOptions? = null

    /**
     * Options that configure the scanning strategy such as include and exclude rules
     */
    @CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1")
    var strategyOptions: StrategyOptions? = null

    /**
     * Options that configure the advanced scanning settings such as depth limits or child limits
     */
    @CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1")
    var advancedOptions: AdvancedOptions? = null

    /**
     * Options for output destinations such as writing to a file instead of printing to console
     */
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

    /**
     * Builds an [AppConfiguration] object combining all specified options, or their defaults if unspecified
     *
     * @param root the directory to scan
     * @return the aggregated application configuration for tree building and rendering
     */
    private fun buildConfiguration(root: File): AppConfiguration {
      return AppConfiguration.builder()
          .root(root)
          .renderConfig(renderOptions?.config() ?: RenderConfig())
          .strategyConfig(strategyOptions?.config() ?: StrategyConfig())
          .advancedConfig(advancedOptions?.config() ?: AdvancedConfig())
          .build()
    }

    /**
     * Executes the tree rendering process using the provided configuration
     *
     * Renders the tree structure, applies styling, outputs to console and/or file as specified
     *
     * @param config the configuration governing tree scanning, rendering, and output
     * @return exit code '0' on success, or '1' if rendering failed due to issues
     */
    private fun executeRender(config: AppConfiguration): Int {
        val tree = TreeBuilder.buildTree(config)

        val symbols = config.renderConfig.customStyle?.let {
            StyleRegistry.get(it) ?: run {
                System.err.println("Custom style '${it}' is not recognized")
                return 1
            }
        } ?: config.renderConfig.style.symbols

        val render = buildString {
            appendLine(root.name + "/")
            append(TreeBuilder.renderTree(tree, symbols = symbols))
        }

        val outputOpt = outputOptions ?: OutputOptions()
        if (outputOpt.shouldPrintToConsole()) {
            println(render)
        }

        outputOpt.outputFile?.let {
            it.writeText(render)
            println("Tree exported to '${it.absolutePath}'")
        }

        return 0
    }

}