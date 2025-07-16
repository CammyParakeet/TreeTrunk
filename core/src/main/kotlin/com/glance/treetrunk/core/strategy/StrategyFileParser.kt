package com.glance.treetrunk.core.strategy

import java.io.File

/**
 * Responsible for parsing a specific strategy file type into [StrategyRule]s
 */
interface StrategyFileParser<T : StrategyRule> {

    val fileName: String
    val fileNamePattern: Regex?

    fun parse(file: File): List<T>

    companion object {
        /**
         * Reads and normalizes lines from a strategy file:
         * - Trims whitespace
         * - Skips blank lines and comments
         *
         * @param file the strategy file
         * @param mapper the mapping function to serialize the rule line
         * @return normalized rule strings
         */
        fun <T> parseLines(file: File, mapper: (String) -> T): List<T> {
            return file.readLines()
                .map { it.trim() }
                .filter { it.isNotBlank() && !it.startsWith("#") }
                .map(mapper)
        }
    }

}