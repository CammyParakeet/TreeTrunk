package com.glance.treetrunk.core.tree

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
 * CLI rendering styles. Determines which symbol set to use
 */
enum class Style(val symbols: CliTreeSymbols) {
    UNICODE(UnicodeSymbols),
    ASCII(AsciiSymbols)

    // TODO: Dynamic registry?
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