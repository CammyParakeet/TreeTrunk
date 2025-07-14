package com.glance.treetrunk.cli

import com.glance.treetrunk.cli.commands.TreeTrunkCli
import com.glance.treetrunk.core.tree.Defaults
import com.glance.treetrunk.core.tree.registerBuiltInStyles
import picocli.CommandLine

/**
 * CLI entrypoint for TreeTrunk
 */
fun main(args: Array<String>) {
    Defaults.registerDefaultIgnoreParsers()
    registerBuiltInStyles()
    CommandLine(TreeTrunkCli()).execute(*args)
}