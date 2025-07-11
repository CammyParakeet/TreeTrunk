package com.glance.treetrunk.cli.commands

import com.glance.treetrunk.core.tree.Style
import com.glance.treetrunk.core.tree.StyleRegistry
import picocli.CommandLine.Command

/**
 * CLI Command to list the available tree rendering styles
 */
@Command(
    name = "list-styles",
    description = ["Lists all available tree rendering styles"]
)
class ListStylesCommand : Runnable {
    override fun run() {
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