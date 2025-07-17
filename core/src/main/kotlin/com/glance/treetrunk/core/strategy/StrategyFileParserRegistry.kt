package com.glance.treetrunk.core.strategy


object StrategyFileParserRegistry {

    private val parsers: MutableMap<Class<out StrategyRule>, MutableList<StrategyFileParser<out StrategyRule>>>
        = mutableMapOf()

    val parserMap: Map<Class<out StrategyRule>, MutableList<StrategyFileParser<out StrategyRule>>>
        get() = parsers.toMap()

    /**
     * Register a parser for a specific StrategyRule type
     */
    fun <T : StrategyRule> registerParser(ruleType: Class<T>, parser: StrategyFileParser<T>) {
        val registered = parsers.getOrPut(ruleType) { mutableListOf() }
        registered.add(parser)
    }

    /**
     * Finds a parser for the given rule type and filename
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : StrategyRule> getParserFor(fileName: String): StrategyFileParser<T>? {
        return parserMap[T::class.java]?.find {
            it.fileName == fileName ||
            it.fileNamePattern?.matches(fileName) == true
        } as? StrategyFileParser<T>
    }

}