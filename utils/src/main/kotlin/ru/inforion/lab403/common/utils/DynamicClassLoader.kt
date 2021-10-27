package ru.inforion.lab403.common.utils

import ru.inforion.lab403.common.logging.FINE
import ru.inforion.lab403.common.logging.logger
import java.io.File
import java.net.URLClassLoader


object DynamicClassLoader : URLClassLoader(arrayOf(), DynamicClassLoader::class.java.classLoader) {

    private const val libraryPathKey = "java.library.path"

    val log = logger(FINE)

    /**
     * {EN} Unset sys_paths to make JVM reload it from java.library.path {EN}
     */
    private fun unsetSystemPath() {
        val sysPathsField = ClassLoader::class.java.getDeclaredField("sys_paths")
        sysPathsField.isAccessible = true
        sysPathsField.set(null, null)
    }

    private val onLoadIntoClasspath = mutableSetOf<OnUrlLoadCallback>()

    /**
     * {EN} Executes specified callback when new url loaded {EN}
     */
    fun onLoadIntoClasspath(action: OnUrlLoadCallback) = onLoadIntoClasspath.add(action)

    /**
     * {EN} Add new class path for e.i. JAR-file {EN}
     */
    fun loadIntoClasspath(classpath: File) {
        val newUrl = classpath.toURI().toURL()

        if (newUrl !in urLs) {
            addURL(newUrl)
            onLoadIntoClasspath.forEach { it(newUrl) }
            log.finer { "Added to classloader: $newUrl" }
        }
    }

    /**
     * {EN} Add path to java.library.path and then unset sys_paths to make JVM reload path {EN}
     *
     * TODO: An illegal reflective access operation has occurred here and it will be denied in a future JVM release
     */
    @Deprecated("Will be denied in future JVM releases")
    fun addLibraryPath(path: String) {
        val javaLibraryPath = System.getProperty(libraryPathKey).split(":")
        if (path in javaLibraryPath) {
            log.warning { "Path $path already in $libraryPathKey..." }
            return
        }

        val newPaths = (javaLibraryPath + path).joinToString(":")

        System.setProperty(libraryPathKey, newPaths)

        log.fine { "Added $path to $libraryPathKey" }

        unsetSystemPath()
    }
}
