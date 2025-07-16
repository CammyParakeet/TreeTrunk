package com.glance.treetrunk.cli.commands.render

import com.glance.treetrunk.core.config.StrategyConfig
import picocli.CommandLine.Option

class StrategyOptions {

    /**
     * Whether to propagate local strategy rules ignore into subdirectories
     */
    @Option(
        names = ["--inlusion-priority"],
        description = ["Include rules take priority over ignore rules"]
    )
    var inclusionPriority: Boolean = false

    /**
     * Whether to propagate local strategy rules ignore into subdirectories
     */
    @Option(
        names = ["--no-default-ignore"],
        description = ["Do not use default ignore rules"]
    )
    var noDefaultIgnores: Boolean = false

    /**
     * Whether to use local ignore strategy rules
     */
    @Option(
        names = ["--no-local-ignore"],
        description = ["Do not use local ignore files"]
    )
    var noLocalIgnores: Boolean = false

    /**
     * Whether to propagate local strategy rules include into subdirectories
     */
    @Option(
        names = ["--no-local-include"],
        description = ["Do not use local include files"]
    )
    var noLocalIncludes: Boolean = false

    /**
     * Whether to propagate local strategy rules ignore into subdirectories
     */
    @Option(
        names = ["--no-propagate-ignore"],
        description = ["Do not propagate ignore rules to subdirectories"]
    )
    var noPropagateIgnore: Boolean = false

    /**
     * Whether to propagate local strategy rules include into subdirectories
     */
    @Option(
        names = ["--no-propagate-include"],
        description = ["Do not propagate include rules to subdirectories"]
    )
    var noPropagateInclude: Boolean = false

    @Option(
        names = ["--ignore", "--exclude", "--ex"],
        description = ["List of ignore rule patterns for this render"]
    )
    var ignoreList: List<String> = listOf()

    @Option(
        names = ["--include", "--in"],
        description = ["List of include rule patterns for this render"]
    )
    var includeList: List<String> = listOf()

    @Option(
        names = ["--ignore-file"],
        description = ["List of ignore rule file paths"]
    )
    var ignoreFiles: List<String> = listOf()

    @Option(
        names = ["--include-file"],
        description = ["List of include rule file paths"]
    )
    var includeFiles: List<String> = listOf()

    @Option(
        names = ["--ignore-preset"],
        description = ["List of ignore rule presets"]
    )
    var ignorePresets: List<String> = listOf()

    @Option(
        names = ["--include-preset"],
        description = ["List of include rule presets"]
    )
    var includePresets: List<String> = listOf()

    fun config(): StrategyConfig {
        return StrategyConfig(
            ignoreList,
            includeList,
            ignoreFiles,
            includeFiles,
            ignorePresets,
            includePresets,
            !noDefaultIgnores,
            !noLocalIgnores,
            !noLocalIncludes,
            !noPropagateIgnore,
            !noPropagateInclude,
            inclusionPriority
        )
    }

}