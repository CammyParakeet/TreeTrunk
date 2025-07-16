package com.glance.treetrunk.cli.commands.render

import picocli.CommandLine.Option
import java.io.File

/**
 * Output configuration options
 */
class OutputOptions {

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

    fun shouldPrintToConsole(): Boolean = outputFile == null || !noPrint

}