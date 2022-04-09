package me.cookie.abyssclient.render.ultralight.js

import com.labymedia.ultralight.UltralightView
import com.labymedia.ultralight.databind.context.ContextProvider
import com.labymedia.ultralight.databind.context.ContextProviderFactory
import com.labymedia.ultralight.javascript.JavascriptContextLock
import com.labymedia.ultralight.javascript.JavascriptValue
import me.cookie.abyssclient.utils.client.ThreadLock
import java.util.function.Consumer

/**
 * This class is used in case Ultralight needs a Javascript context.
 */
class ViewContextProvider(private val view: ThreadLock<UltralightView>) : ContextProvider {

    override fun syncWithJavascript(callback: Consumer<JavascriptContextLock>) {
        view.get().lockJavascriptContext().use { lock -> callback.accept(lock) }
    }

    class Factory(private val view: ThreadLock<UltralightView>) : ContextProviderFactory {

        override fun bindProvider(value: JavascriptValue): ContextProvider {
            // We only have one view, so we can ignore the value.
            // Else use the formula pointed at above to find a view for a given value.
            return ViewContextProvider(view)
        }
    }
}