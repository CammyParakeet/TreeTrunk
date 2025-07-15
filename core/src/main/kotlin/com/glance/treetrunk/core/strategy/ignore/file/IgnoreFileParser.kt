package com.glance.treetrunk.core.strategy.ignore.file

import com.glance.treetrunk.core.strategy.ignore.IgnoreRule
import java.io.File

/**
 * Responsible for parsing a specific type of ignore file into [IgnoreRule]s
 */
interface IgnoreFileParser {
    /**
     * The name of the ignore file this parser handles
     */
    val fileName: String

    /**
     * Optional regex for flexible ignore filename resolution
     */
    val fileNamePattern: Regex?

    /**
     * Parses the ignore file into one or more [IgnoreRule]s
     */
    fun parse(file: File): List<IgnoreRule>

}