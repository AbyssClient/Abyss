package me.cookie.abyssclient.render.ultralight

import me.cookie.abyssclient.config.ConfigSystem
import me.cookie.abyssclient.utils.client.logger
import me.cookie.abyssclient.utils.io.HttpClient
import me.cookie.abyssclient.utils.io.extractZip
import me.cookie.abyssclient.utils.system.IS_LINUX
import me.cookie.abyssclient.utils.system.IS_MAC
import me.cookie.abyssclient.utils.system.IS_WINDOWS
import java.io.File
import kotlin.system.exitProcess


class UltralightResources {

    companion object {

        /**
         * Exact library version of the LabyMod Ultralight Bindings.
         */
        private const val LIBRARY_VERSION = 0.46

    }

    private val ultralightRoot = File(ConfigSystem.rootFolder, "ultralight")
    val binRoot = File(ultralightRoot, "bin")
    val cacheRoot = File(ultralightRoot, "cache")
    val resourcesRoot = File(ultralightRoot, "resources")

    /**
     * Download resources
     */
    fun downloadResources() {
        runCatching {
            val versionsFile = File(ultralightRoot, "VERSION")

            // Check if library version is matching the resources version
            if (versionsFile.exists() && versionsFile.readText().toDoubleOrNull() == LIBRARY_VERSION) {
                return
            }

            // Make sure the old natives are being deleted
            if (binRoot.exists()) {
                binRoot.deleteRecursively()
            }

            if (resourcesRoot.exists()) {
                resourcesRoot.deleteRecursively()
            }

            // Translate os to path
            val os = when {
                IS_WINDOWS -> "win"
                IS_MAC -> "mac"
                IS_LINUX -> "linux"
                else -> error("unsupported operating system")
            }

            logger.info("Downloading v$LIBRARY_VERSION resources... (os: $os)")
            val nativeUrl =
                "${me.cookie.abyssclient.Abyss.LIQUID_CLIENT_CLOUD}/ultralight_resources/$LIBRARY_VERSION/$os-x64.zip"

            // Download resources
            ultralightRoot.mkdir()
            val pkgNatives = File(ultralightRoot, "resources.zip").apply {
                createNewFile()
                HttpClient.download(nativeUrl, this)
            }

            // Extract resources from zip archive
            logger.info("Extracting resources...")
            extractZip(pkgNatives, ultralightRoot)
            versionsFile.createNewFile()
            versionsFile.writeText(LIBRARY_VERSION.toString())

            // Make sure to delete zip archive to save space
            logger.debug("Deleting resources bundle...")
            pkgNatives.delete()

            logger.info("Successfully loaded resources.")
        }.onFailure {
            logger.error("Unable to download resources", it)

            exitProcess(-1)
        }
    }

}