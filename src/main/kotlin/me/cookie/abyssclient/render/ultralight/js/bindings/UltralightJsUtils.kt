package me.cookie.abyssclient.render.ultralight.js.bindings

import me.cookie.abyssclient.utils.client.browseUrl

/**
 * Referenced by JS as `utils`
 */
object UltralightJsUtils {

    /**
     * Open link
     */
    fun browse(url: String) = browseUrl(url)

}