package ru.inforion.lab403.common.json

import kotlinx.serialization.json.Json

@PublishedApi internal val jsons = Array(16) {
    Json {
        ignoreUnknownKeys = true
    }
}

inline val json get() = jsons.random()