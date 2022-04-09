package me.cookie.abyssclient.render.ultralight.theme

import me.cookie.abyssclient.config.ConfigSystem
import me.cookie.abyssclient.utils.client.logger
import me.cookie.abyssclient.utils.io.extractZip
import me.cookie.abyssclient.utils.io.resource
import java.io.File
import java.nio.file.StandardWatchEventKinds.*

object ThemeManager {

    val themesFolder = File(ConfigSystem.rootFolder, "themes")
    val defaultTheme = Theme.default()

    var activeTheme = defaultTheme

    fun page(name: String) = activeTheme.page(name)

}

class Theme(val name: String) {

    internal val themeFolder = File(ThemeManager.themesFolder, name)

    val exists: Boolean
        get() = themeFolder.exists()

    companion object {

        fun default() = Theme("default").apply {
            runCatching {
                val stream = resource("/assets/phantom/default_theme.zip")

                if (exists) {
                    themeFolder.deleteRecursively()
                }

                extractZip(stream, themeFolder)
                themeFolder.deleteOnExit()
            }.onFailure {
                logger.error("Unable to extract default theme", it)
            }.onSuccess {
                logger.info("Successfully extracted default theme")
            }

        }

    }

    fun page(name: String): Page? {
        val page = Page(this, name)

        if (page.exists) {
            return page
        }
        return null
    }

}

class Page(theme: Theme, val name: String) {

    private val pageFolder = File(theme.themeFolder, name)

    val viewableFile: String
        get() = "file:///${File(pageFolder, "index.html").absolutePath}"

    val exists: Boolean
        get() = pageFolder.exists()

    private val watcher by lazy {
        val path = pageFolder.toPath()
        val watchService = path.fileSystem.newWatchService()
        path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY)
        watchService
    }

    fun hasUpdate(): Boolean {
        val watchKey = watcher.poll()
        val shouldUpdate = watchKey?.pollEvents()?.isNotEmpty() == true
        watchKey?.reset()
        return shouldUpdate
    }

    fun close() {
        watcher.close()
    }

    override fun toString() = "Page($name, $viewableFile)"

}