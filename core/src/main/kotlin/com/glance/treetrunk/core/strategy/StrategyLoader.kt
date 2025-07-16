package com.glance.treetrunk.core.strategy

import java.io.File
import java.io.IOException
import java.util.logging.Logger

/**
 * Loads strategy files either from the classpath resources or from a specified path,
 * based on the provided strategy type
 *
 * Strategy files are expected to either:
 * - Reside in 'resources/strategies' in the classpath
 * - Exist in the provided path for user-supplied strategy files
 */
object StrategyLoader {

    private val logger = Logger.getLogger(StrategyLoader::class.java.name)

    private val suffixes = mapOf(
        Strategy.IGNORE to listOf(".treeignore", ".trunkignore"),
        Strategy.INCLUDE to listOf(".treeinclude", ".trunkinclude")
    )

    /**
     * Loads a strategy file by name for the specified strategy type, searching first from the provided
     * filesystem directory (if supplied), then falling back to the bundled classpath resources
     *
     * @param name the base name of the strategy file (e.g. 'gradle', 'maven')
     * @param strategy the type of strategy (IGNORE or INCLUDE)
     * @param fromDirectory optional filesystem directory to look for the strategy file
     * @param require if true, throws [IOException] if no matching strategy file is found
     * @return the file's lines if found, or null if no matching file exists
     */
    @Throws(IOException::class)
    fun loadStrategyFile(
        name: String,
        strategy: Strategy,
        fromDirectory: File? = null,
        require: Boolean = false
    ): List<String>? {
        val suffixList = suffixes[strategy] ?: return null
        for (suffix in suffixList) {
            val filename = "$name$suffix"

            // Check filesystem first if provided
            if (fromDirectory != null) {
                val file = File(fromDirectory, filename)
                if (file.exists()) {
                    if (file.isFile) {
                        try {
                            logger.fine("Loading strategy from filesystem: ${file.absolutePath}")
                            return file.readLines()
                        } catch (e: IOException) {
                            throw IOException("Failed to read strategy file from filesystem: ${file.absolutePath}", e)
                        }
                    } else {
                        logger.fine("Skipping $filename in filesystem: not a regular file")
                    }
                } else {
                    logger.fine("Strategy file not found in filesystem: ${file.absolutePath}")
                }
            }

            // Fallback to resources
            val path = "strategy/${strategy.name.lowercase()}/$filename"
            val stream = StrategyLoader::class.java.classLoader.getResourceAsStream(path)
            if (stream != null) {
                try {
                    logger.fine("Loading strategy from resource: $path")
                    return stream.bufferedReader().readLines()
                } catch (e: IOException) {
                    throw IOException("Failed to read strategy resource file: $path", e)
                }
            } else {
                logger.fine("Strategy resource not found: $path")
            }
        }
        if (require) {
            throw IOException("No strategy file found for '$name' with strategy $strategy")
        }

        return null
    }

}