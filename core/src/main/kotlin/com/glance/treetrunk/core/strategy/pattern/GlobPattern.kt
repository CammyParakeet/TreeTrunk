package com.glance.treetrunk.core.strategy.pattern

import com.glance.treetrunk.core.strategy.DepthMode

/**
 * Compiles and matches a glob pattern against normalized path string
 *
 * Supports depth sensitive matching via [DepthMode]
 *
 * @param glob The raw glob pattern
 * @param depthMode Controls whether the match applies only at the root, any depth, or exact paths
 */
class GlobPattern(
    private val glob: String,
    private val depthMode: DepthMode = DepthMode.ANY_DEPTH
) {

    private val regex: Regex = compileGlobToRegex(glob, depthMode)

    fun getPattern(): String {
        return regex.pattern
    }

    /**
     * Tests whether the given path matches the glob pattern
     *
     * Path separators are normalized to '/' for cross-platform consistency
     */
    fun matches(path: String): Boolean {
        val normalizedPath = path.replace("\\", "/")
        val result = regex.matches(normalizedPath)
        //println("GlobPattern matching '$normalizedPath' against '$regex' => $result")
        return result
    }

    override fun toString(): String {
        return "GlobPattern(glob='$glob', depthMode=$depthMode, regex=$regex)"
    }

    companion object {
        /**
         * Compiles the given glob and depth mode into a regex pattern
         */
        private fun compileGlobToRegex(rawGlob: String, depthMode: DepthMode): Regex {
            val isDirectoryPattern = rawGlob.endsWith("/")

            val sb = StringBuilder()
            sb.append("^")

            var adjusted = rawGlob

            if (!rawGlob.contains("/") && !rawGlob.startsWith("**/")) {
                adjusted = "**/$rawGlob"
            }

            when (depthMode) {
                DepthMode.ROOT_ONLY -> sb.append("^")
                DepthMode.ANY_DEPTH -> sb.append("(^|.*/)")
                else -> {}
            }

            var i = 0
            while (i < adjusted.length) {
                when {
                    adjusted[i] == '*' && i + 1 < adjusted.length && adjusted[i + 1] == '*' -> {
                        // ** -> match any number of directories
                        if (i + 2 < adjusted.length && adjusted[i + 2] == '/') {
                            sb.append("(^|.*/)")
                            i += 2 // skip ** and /
                        } else {
                            sb.append(".*")
                            i++
                        }
                    }
                    adjusted[i] == '*' -> sb.append("[^/]*")
                    adjusted[i] == '?' -> sb.append("[^/]")
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



}