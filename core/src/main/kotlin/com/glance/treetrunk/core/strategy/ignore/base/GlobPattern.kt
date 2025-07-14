package com.glance.treetrunk.core.strategy.ignore.base

class GlobPattern(private val glob: String) {

    private val regex: Regex = compileGlobToRegex(glob)

    fun matches(path: String): Boolean {
        val normalizedPath = path.replace("\\", "/")
        val result = regex.matches(normalizedPath)
        println("GlobPattern matching '$normalizedPath' against '$regex' => $result")
        return result
    }

    private fun compileGlobToRegex(rawGlob: String): Regex {
        val isDirectoryPattern = rawGlob.endsWith("/")

        val sb = StringBuilder()
        sb.append("^")

        var adjusted = rawGlob

        if (!rawGlob.contains("/") && !rawGlob.startsWith("**/")) {
            adjusted = "**/$rawGlob"
        }

        var i = 0
        while (i < adjusted.length) {
            when {
                adjusted[i] == '*' && i + 1 < adjusted.length && adjusted[i + 1] == '*' -> {
                    if (i + 2 < adjusted.length && adjusted[i + 2] == '/') {
                        sb.append("(^|.*/)")
                        i += 2 // skip ** and /
                    } else {
                        sb.append(".*")
                        i++
                    }
                }
                adjusted[i] == '*' -> {
                    sb.append("[^/]*")
                }
                adjusted[i] == '?' -> {
                    sb.append("[^/]")
                }
                else -> sb.append(Regex.escape(adjusted[i].toString()))
            }
            i++
        }

        if (isDirectoryPattern) {
            sb.append("(.*)?")
        }

        sb.append("$")
        return sb.toString().toRegex()
    }

}