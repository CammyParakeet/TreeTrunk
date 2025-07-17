package com.glance.treetrunk.core.config

import java.io.File

/**
 * Full configuration object aggregating all options required to build and render
 * a directory tree
 *
 * @property root the root directory to start scanning from
 * @property renderConfig rendering options defining the output style and constraints
 * @property strategyConfig options defining ignore/include strategies for scanning
 * @property advancedConfig advanced settings for handling depth, child limits, and smart expansion
 */
data class AppConfiguration(
    val root: File,
    val renderConfig: RenderConfig,
    val strategyConfig: StrategyConfig,
    val advancedConfig: AdvancedConfig
) {
    companion object {
        /**
         * Creates a new [Builder] for constructing an [AppConfiguration] instance fluently
         */
        fun builder(): Builder = Builder()
    }

    /**
     * Builder for [AppConfiguration] to simplify progressive and fluent construction
     * of configuration instances
     */
    class Builder {
        private lateinit var root: File
        private lateinit var renderConfig: RenderConfig
        private lateinit var strategyConfig: StrategyConfig
        private lateinit var advancedConfig: AdvancedConfig

        fun root(root: File) = apply { this.root = root }
        fun renderConfig(options: RenderConfig) = apply { this.renderConfig = options }
        fun strategyConfig(options: StrategyConfig) = apply { this.strategyConfig = options }
        fun advancedConfig(options: AdvancedConfig) = apply { this.advancedConfig = options }

        /**
         * Builds and returns the [AppConfiguration] with all supplied parameters
         *
         * @throws UninitializedPropertyAccessException if any of the required configurations are not set
         */
        fun build() = AppConfiguration(root, renderConfig, strategyConfig, advancedConfig)
    }
}
