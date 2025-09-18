package com.anje.kelvin.aconting.util

import java.util.UUID
import java.util.concurrent.atomic.AtomicLong
import kotlin.random.Random

/**
 * Utility class for generating unique IDs to prevent collisions.
 * Provides different ID generation strategies for various use cases.
 */
object IdGenerator {

    private val atomicCounter = AtomicLong(System.currentTimeMillis())
    
    /**
     * Generates a unique Long ID using an atomic counter.
     * This is collision-free even in rapid succession calls.
     * 
     * @return A unique Long ID
     */
    fun generateLongId(): Long {
        return atomicCounter.incrementAndGet()
    }
    
    /**
     * Generates a unique String ID using UUID.
     * This provides the highest guarantee of uniqueness.
     * 
     * @return A unique String ID (UUID format)
     */
    fun generateStringId(): String {
        return UUID.randomUUID().toString()
    }
    
    /**
     * Generates a compact unique String ID using timestamp + random suffix.
     * This provides a good balance between uniqueness and readability.
     * 
     * @return A compact unique String ID
     */
    fun generateCompactStringId(): String {
        val timestamp = System.currentTimeMillis()
        val random = Random.nextInt(AppConstants.ID_RANDOM_MIN, AppConstants.ID_RANDOM_MAX)
        return "${timestamp}_$random"
    }
    
    /**
     * Generates a unique Long ID with timestamp base + atomic increment.
     * This maintains some temporal ordering while preventing collisions.
     * 
     * @return A unique Long ID with timestamp characteristics
     */
    fun generateTimestampBasedId(): Long {
        // Use current time in seconds as base, then add atomic increment
        val baseTime = System.currentTimeMillis() / AppConstants.ID_TIMESTAMP_DIVISOR * AppConstants.ID_TIMESTAMP_DIVISOR
        val increment = atomicCounter.incrementAndGet() % AppConstants.ID_INCREMENT_MODULO
        return baseTime + increment
    }
}