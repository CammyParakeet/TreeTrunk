package com.glance.treetrunk.core.strategy

/**
 * Represents the type of strategy being applied during tree scanning
 *
 * Determines whether a rule set is being used to ignore or include files and directories
 */
enum class Strategy {
    /** Strategy to exclude files or directories based on matching rules */
    IGNORE,
    /** Strategy to explicitly include files or directories based on matching rules */
    INCLUDE
}