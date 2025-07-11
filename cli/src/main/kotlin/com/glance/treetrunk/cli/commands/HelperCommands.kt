package com.glance.treetrunk.cli.commands

import picocli.CommandLine.IVersionProvider

/**
 * Version info override
 */
class TreeTrunkVersion : IVersionProvider {
    override fun getVersion(): Array<String> {
        val version = TreeTrunkVersion::class.java
            .`package`?.implementationVersion ?: "UNKNOWN"

        return arrayOf("TreeTrunk CLI", "Version: $version")
    }

}