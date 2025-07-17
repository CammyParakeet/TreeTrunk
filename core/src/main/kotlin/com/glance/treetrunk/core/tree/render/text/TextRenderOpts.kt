package com.glance.treetrunk.core.tree.render.text

import com.glance.treetrunk.core.tree.CliTreeSymbols
import com.glance.treetrunk.core.tree.UnicodeSymbols

data class TextRenderOpts(
    val showLineNumbers: Boolean = false,
    val showFileSizes: Boolean = false,
    val maxLines: Int? = null,
    val symbols: CliTreeSymbols = UnicodeSymbols,
    val metadata: Map<String, Any> = emptyMap()
)