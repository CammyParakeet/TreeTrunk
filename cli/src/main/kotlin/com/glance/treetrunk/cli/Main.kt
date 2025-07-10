package com.glance.treetrunk.cli

import com.glance.treetrunk.core.TreeBuilder
import picocli.CommandLine
import picocli.CommandLine.Command
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

    @Parameters(index = "0", description = ["Root directory to scan"])
    lateinit var root: File

    override fun call(): Int {
        if (!root.exists() || !root.isDirectory) {
            System.err.println("Invalid directory: ${root.absolutePath}")
            return 1
        }

        val tree = TreeBuilder.buildTree(root)
        println(root.name + "/")
        println(TreeBuilder.renderTree(tree))
        return 0
    }
}

fun main(args: Array<String>) {
    CommandLine(Main()).execute(*args)
}