package com.glance.treetrunk.cli.commands

import com.glance.treetrunk.cli.commands.render.RenderCommand
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Model.CommandSpec
import picocli.CommandLine.Spec

/**
 * Root entrypoint command
 */
@Command(
    name = "treetrunk",
    mixinStandardHelpOptions = true,
    subcommands = [
        RenderCommand::class,
        ListStylesCommand::class
    ],
    versionProvider = TreeTrunkVersion::class,
    description = ["A tool for visualizing and exporting project folder structures"]
)
// TODO: cool ascii logo? :P
class TreeTrunkCli : Runnable {

    @Spec
    lateinit var spec: CommandSpec

    override fun run() {
        CommandLine(spec).usage(System.out)
    }

}