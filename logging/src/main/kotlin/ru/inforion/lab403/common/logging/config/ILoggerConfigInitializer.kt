package ru.inforion.lab403.common.logging.config

interface ILoggerConfigInitializer {
    /**
     * Loads logger config file
     */
    fun load()
}