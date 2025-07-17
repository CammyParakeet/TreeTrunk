package com.glance.treetrunk.core.tree

import com.glance.treetrunk.core.tree.render.text.StyleRegistry

/**
 * Defines the character symbols used to render a tree in CLI output
 *
 * Implementations control how branches and indents are displayed
 */
interface CliTreeSymbols {
    val vertical: String
    val branch: String
    val lastBranch: String
    val indent: String

}

/**
 * Register the built-in supported tree text styles
 */
fun registerBuiltInStyles() {
    StyleRegistry.register(Style.UNICODE.toString(), UnicodeSymbols)
    StyleRegistry.register(Style.ASCII.toString(), AsciiSymbols)
    //StyleRegistry.register(Style.MARKDOWN.toString(), )
    //StyleRegistry.register(Style.EMOJI.toString(), )
}

/**
 * CLI rendering styles. Determines which symbol set to use
 */
enum class Style(private val styleName: String) {
    UNICODE("UNICODE"),
    ASCII("ASCII"),
    MARKDOWN("MARKDOWN"),
    EMOJI("EMOJI");

    val symbols: CliTreeSymbols
        get() = StyleRegistry.get(styleName) ?: error("Style '$styleName' not recognized")

    override fun toString(): String = styleName
}

/**
 * Unicode-based rendering symbols for CLI tree output
 *
 * Default style with clean line drawing characters
 */
object UnicodeSymbols : CliTreeSymbols {
    override val vertical = "│   "
    override val branch = "├── "
    override val lastBranch = "└── "
    override val indent = "    "
}

/**
 * ASCII-safe alternative to UnicodeSymbols for terminals that don't support line-drawing chars
 */
object AsciiSymbols : CliTreeSymbols {
    override val vertical = "|   "
    override val branch = "|-- "
    override val lastBranch = "`-- "
    override val indent = "     "
}