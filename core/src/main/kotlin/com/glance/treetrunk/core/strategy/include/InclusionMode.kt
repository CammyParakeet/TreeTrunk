package com.glance.treetrunk.core.strategy.include

enum class InclusionMode {
    OVERRIDE_IGNORE,        // soft: included entries bypass ignore rules
    FILTER                  // hard: only include matching include rules
}