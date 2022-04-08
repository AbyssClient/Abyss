package de.vincentschweiger.phantomclient.render.ultralight.js.bindings

import de.vincentschweiger.phantomclient.utils.client.browseUrl

/**
 * Referenced by JS as `utils`
 */
object UltralightJsUtils {

    /**
     * Open link
     */
    fun browse(url: String) = browseUrl(url)

}