package com.glance.treetrunk.core.config

import java.io.File

data class AppConfiguration(
    val root: File,
    val renderConfig: RenderConfig,
    val strategyConfig: StrategyConfig,
    val advancedConfig: AdvancedConfig
) {
    companion object {
        fun builder(): Builder = Builder()
    }

    class Builder {
        private lateinit var root: File
        private lateinit var renderConfig: RenderConfig
        private lateinit var strategyConfig: StrategyConfig
        private lateinit var advancedConfig: AdvancedConfig

        fun root(root: File) = apply { this.root = root }
        fun renderConfig(options: RenderConfig) = apply { this.renderConfig = options }
        fun strategyConfig(options: StrategyConfig) = apply { this.strategyConfig = options }
        fun advancedConfig(options: AdvancedConfig) = apply { this.advancedConfig = options }

        fun build() = AppConfiguration(root, renderConfig, strategyConfig, advancedConfig)
    }
}
