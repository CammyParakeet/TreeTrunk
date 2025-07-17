package com.glance.treetrunk.cli.commands.render

import com.glance.treetrunk.core.config.StrategyConfig
import com.glance.treetrunk.core.strategy.include.InclusionMode
import picocli.CommandLine.Option

/**
 * Configuration options for scanning and inclusion/exclusion strategies when building the tree
 *
 * These options control how ignore/include rules are applied, whether local files and presets
 * are used, and whether rules propagate through subdirectories
 */
class StrategyOptions {

    /**
     * Set the inclusion mode for when include rules are supplied:
     * - [InclusionMode.OVERRIDE_IGNORE] = Soft Include, overrides ignore rules
     * - [InclusionMode.FILTER] = Hard Include, only accepts files that match include rules, when supplied
     */
    @Option(
        names = ["--inlusion-mode"],
        description = ["Inclusion mode as soft or hard: \${COMPLETION-CANDIDATES}"]
    )
    var inclusionMode: InclusionMode = InclusionMode.OVERRIDE_IGNORE

    /**
     * Whether to use the installed default ignore rules, includes common rules like '.git'
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
     * Whether to use local include stategy rules
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

    /**
     * A list of ignore patterns (glob-like) provided via command line to exclude files/directories
     */
    @Option(
        names = ["--ignore", "--exclude", "--ex"],
        description = ["List of ignore rule patterns for this render"]
    )
    var ignoreList: List<String> = listOf()

    /**
     * A list of include patterns (glob-like) provided via command line to force inclusion of files/directories
     */
    @Option(
        names = ["--include", "--in"],
        description = ["List of include rule patterns for this render"]
    )
    var includeList: List<String> = listOf()

    /**
     * A list of file paths that contain ignore rules to be loaded explicitly
     */
    @Option(
        names = ["--ignore-file", "--ef"],
        description = ["List of ignore rule file paths"]
    )
    var ignoreFiles: List<String> = listOf()

    /**
     * A list of file paths that contain include rules to be loaded explicitly
     */
    @Option(
        names = ["--include-file", "--if"],
        description = ["List of include rule file paths"]
    )
    var includeFiles: List<String> = listOf()

    /**
     * A list of predefined ignore rule presets (e.g. for common project types) to apply
     */
    @Option(
        names = ["--ignore-preset", "--e"],
        description = ["List of ignore rule presets"]
    )
    var ignorePresets: List<String> = listOf()

    /**
     * A list of predefined include rule presets to apply
     */
    @Option(
        names = ["--include-preset", "--i"],
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
            inclusionMode
        )
    }

}