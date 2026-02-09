package org.example.geoblinker.data.database

import app.cash.sqldelight.db.SqlDriver

/**
 * Platform-specific database driver factory
 * 
 * Actual implementations:
 * - Android: Uses AndroidSqliteDriver
 * - iOS: Uses NativeSqliteDriver
 */
expect class DatabaseDriverFactory {
    
    /**
     * Creates a platform-specific SQL driver
     * 
     * @return SqlDriver instance for the current platform
     */
    fun createDriver(): SqlDriver
}
