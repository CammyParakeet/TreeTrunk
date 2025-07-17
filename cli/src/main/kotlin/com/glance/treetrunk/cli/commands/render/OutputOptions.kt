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

    /**
     * If true, the rendered tree will still be printed to the console
     * even when an [outputFile] is specified
     */
    @Option(
        names = ["--keep-print"],
        description = ["Only read if an output is supplied, keeps the render to console"]
    )
    var keepPrint: Boolean = false

    /**
     * If true, the rendered tree will not be printed to the console
     *
     * Used in combination with [outputFile] to suppress console output
     */
    @Option(
        names = ["--no-print"],
        description = ["Do not print to console (only write to file)"]
    )
    var noPrint: Boolean = false

    /**
     * Determines whether the rendered tree should be printed to the console
     * based on the combination of [outputFile], [keepPrint], and [noPrint]
     *
     * @return true if the tree should be printed to the console
     */
    fun shouldPrintToConsole(): Boolean {
        if (noPrint) return false
        return outputFile == null || keepPrint
    }

}