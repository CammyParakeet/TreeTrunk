package com.glance.treetrunk.core.tree

/**
 * Central registry for CLI tree rendering styles
 *
 * Supports both built-in and dynamically registered symbol styles
 */
object StyleRegistry {

    private val styles: MutableMap<String, CliTreeSymbols> = mutableMapOf()

    /**
     * Registers a new style with a given name
     */
    fun register(name: String, symbols: CliTreeSymbols) {
        styles[name.uppercase()] = symbols
    }

    /**
     * Retrieves a style by name (case-insensitive)
     */
    fun get(name: String): CliTreeSymbols? = styles[name.uppercase()]

    /**
     * Returns a map of all registered styles
     */
    fun all(): Map<String, CliTreeSymbols> = styles.toMap()

    /**
     * Clears all registered styles
     */
    fun clear() {
        styles.clear()
    }

}