package com.glance.treetrunk.core.strategy.ignore.file

/**
 * Registry of all known [IgnoreFileParser]s
 */
object IgnoreFileParserRegistry {
    private val parsers = mutableListOf<IgnoreFileParser>()

    fun register(parser: IgnoreFileParser) {
        parsers += parser
    }

    fun getParsers(): List<IgnoreFileParser> = parsers.toList()

    /**
     * Finds a parser based on a file name match
     */
    fun findParserFor(fileName: String): IgnoreFileParser? {
        return parsers.find { it.fileName == fileName }
    }

}