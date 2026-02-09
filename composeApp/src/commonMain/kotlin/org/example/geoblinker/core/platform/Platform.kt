package org.example.geoblinker.core.platform

/**
 * Platform-specific utilities
 * Provides information about the current platform
 */
expect object Platform {
    /**
     * Platform name (e.g., "Android", "iOS")
     */
    val name: String
    
    /**
     * Platform version
     */
    val version: String
    
    /**
     * Device model/identifier
     */
    val deviceModel: String
}

/**
 * Platform type enumeration
 */
enum class PlatformType {
    ANDROID,
    IOS,
    DESKTOP,
    WEB
}

/**
 * Get current platform type
 */
expect fun getPlatformType(): PlatformType
