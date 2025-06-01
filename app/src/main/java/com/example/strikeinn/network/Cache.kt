package com.example.strikeinn.network

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Cache<T>() {
    private var cachedData: List<T>? = null
    private var timestamp: Long = 0L
    private val mutex = Mutex()

    suspend fun get(cacheDurationMs: Long): List<T>? = mutex.withLock {
        val now = System.currentTimeMillis()
        if (cachedData != null && now - timestamp < cacheDurationMs) {
            cachedData
        } else {
            cachedData = null
            null
        }
    }

    suspend fun put(data: List<T>) = mutex.withLock {
        cachedData = data
        timestamp = System.currentTimeMillis()
    }

    suspend fun invalidate() = mutex.withLock {
        cachedData = null
        println("Cache invalidated")
    }
}