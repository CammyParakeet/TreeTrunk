package com.glance.treetrunk.core.strategy.ignore

object GlobToRegexConverter {

    fun convert(glob: String): Regex {
        val regex = Regex.escape(glob)
            .replace("\\*\\*", ".*")     // Replace ** with .*
            .replace("\\*", "[^/]*")     // Replace * with anything except /
            .replace("\\?", "[^/]")        // Replace ? with any single char

        return Regex("^$regex$")
    }

}