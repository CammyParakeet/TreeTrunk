package com.glance.treetrunk.core.test.ignore

import com.glance.treetrunk.core.strategy.ignore.base.GlobPattern
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class GlobPatternTest: StringSpec({
    "should match files with *.log" {
        val pattern = GlobPattern("*.log")
        pattern.matches("error.log") shouldBe true
        pattern.matches("logs/error.log") shouldBe true
        pattern.matches("error.txt") shouldBe false
    }

    "should normalize Windows-style backslashes in paths" {
        val pattern = GlobPattern("*.log")

        pattern.matches("error.log") shouldBe true
        pattern.matches("logs\\debug.log") shouldBe true
        pattern.matches("deep\\logs\\debug.log") shouldBe true
        pattern.matches("error.txt") shouldBe false
    }

    "should match directories with trailing slash strictly" {
        val pattern = GlobPattern("out/")
        pattern.matches("out") shouldBe false
        pattern.matches("out/") shouldBe true
        pattern.matches("out/production/classes") shouldBe true
        pattern.matches("out/production/classes/") shouldBe true
        pattern.matches("outfile/production/classes") shouldBe false
        pattern.matches("outfile/") shouldBe false
        pattern.matches("nested/out") shouldBe false
        pattern.matches("nested/out/") shouldBe true
        pattern.matches("nested/output") shouldBe false
    }

    "should match directories with backslashes on Windows paths" {
        val pattern = GlobPattern("build/")

        pattern.matches("build") shouldBe false
        pattern.matches("build\\output.txt") shouldBe true
        pattern.matches("nested\\build\\") shouldBe true
        pattern.matches("nested\\build") shouldBe false
        pattern.matches("nested\\mybuild") shouldBe false
    }

    "should match recursive patterns like **/cache/**" {
        val pattern = GlobPattern("**/cache/**")
        pattern.matches("cache") shouldBe false
        pattern.matches("nested/cache/image.png") shouldBe true
        pattern.matches("deep/nested/cache/file.txt") shouldBe true
        pattern.matches("deep/nested/somefolder/file.txt") shouldBe false
    }

    "should match mixed separators in paths" {
        val pattern = GlobPattern("**/cache/**")

        pattern.matches("cache") shouldBe false
        pattern.matches("nested/cache/image.png") shouldBe true
        pattern.matches("nested\\cache\\image.png") shouldBe true
        pattern.matches("deep/nested\\cache\\file.txt") shouldBe true
    }

    "should match single character wildcard ?" {
        val pattern = GlobPattern("file?.txt")
        pattern.matches("file1.txt") shouldBe true
        pattern.matches("fileA.txt") shouldBe true
        pattern.matches("fileAB.txt") shouldBe false
        pattern.matches("file.txt") shouldBe false
    }

    "should match any .log file at any depth with **/*.log" {
        val pattern = GlobPattern("**/*.log")
        pattern.matches("error.log") shouldBe true
        pattern.matches("logs/error.log") shouldBe true
        pattern.matches("deep/logs/error.log") shouldBe true
        pattern.matches("deep/logs/error.txt") shouldBe false
    }

    "should match files with a literal dot in the name" {
        val pattern = GlobPattern("file.name.*")
        pattern.matches("file.name.txt") shouldBe true
        pattern.matches("file.name.log") shouldBe true
        pattern.matches("filename.log") shouldBe false
    }

    "should match complex nested paths with ** and *" {
        val pattern = GlobPattern("**/test*/**")
        pattern.matches("test/file.txt") shouldBe true
        pattern.matches("nested/test1/file.txt") shouldBe true
        pattern.matches("deep/nested/test_case/files") shouldBe true
        pattern.matches("prod/testcase/files") shouldBe true
        pattern.matches("prod/case/files") shouldBe false
    }

    "should not match when no pattern matches" {
        val pattern = GlobPattern("*.tmp")
        pattern.matches("file.log") shouldBe false
        pattern.matches("nested/file.txt") shouldBe false
    }
})