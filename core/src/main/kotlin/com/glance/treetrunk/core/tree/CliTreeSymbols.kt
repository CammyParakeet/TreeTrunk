package com.glance.treetrunk.core.tree

interface CliTreeSymbols {
    val vertical: String
    val branch: String
    val lastBranch: String
    val indent: String

}

enum class Style(val symbols: CliTreeSymbols) {
    UNICODE(UnicodeSymbols),
    ASCII(AsciiSymbols)
}

object UnicodeSymbols : CliTreeSymbols {
    override val vertical = "│   "
    override val branch = "├── "
    override val lastBranch = "└── "
    override val indent = "    "
}

object AsciiSymbols : CliTreeSymbols {
    override val vertical = "|   "
    override val branch = "|-- "
    override val lastBranch = "`-- "
    override val indent = "     "
}