package de.vincentschweiger.phantomclient.render.ultralight.js

import com.labymedia.ultralight.UltralightView
import com.labymedia.ultralight.databind.Databind
import com.labymedia.ultralight.databind.DatabindConfiguration
import com.labymedia.ultralight.javascript.JavascriptContext
import de.vincentschweiger.phantomclient.render.ultralight.ScreenView
import de.vincentschweiger.phantomclient.render.ultralight.UltralightEngine
import de.vincentschweiger.phantomclient.render.ultralight.View
import de.vincentschweiger.phantomclient.render.ultralight.js.bindings.UltralightJsEvents
import de.vincentschweiger.phantomclient.render.ultralight.js.bindings.UltralightJsKotlin
import de.vincentschweiger.phantomclient.render.ultralight.js.bindings.UltralightJsUi
import de.vincentschweiger.phantomclient.render.ultralight.js.bindings.UltralightJsUtils
import de.vincentschweiger.phantomclient.utils.client.ThreadLock
import de.vincentschweiger.phantomclient.utils.client.mc

/**
 * Context setup
 */
class UltralightJsContext(view: View, ulView: ThreadLock<UltralightView>) {

    val contextProvider = ViewContextProvider(ulView)
    val databind = Databind(
        DatabindConfiguration
            .builder()
            .contextProviderFactory(ViewContextProvider.Factory(ulView))
            .build()
    )

    var events = UltralightJsEvents(contextProvider, view)

    fun setupContext(view: View, context: JavascriptContext) {
        val globalContext = context.globalContext
        val globalObject = globalContext.globalObject

        globalObject.setProperty(
            "engine",
            databind.conversionUtils.toJavascript(context, UltralightEngine),
            0
        )

        globalObject.setProperty(
            "view",
            databind.conversionUtils.toJavascript(context, view),
            0
        )

        globalObject.setProperty(
            "events",
            databind.conversionUtils.toJavascript(context, events),
            0
        )

        // todo: minecraft has to be remapped
        globalObject.setProperty(
            "minecraft",
            databind.conversionUtils.toJavascript(context, mc),
            0
        )

        globalObject.setProperty(
            "ui",
            databind.conversionUtils.toJavascript(context, UltralightJsUi),
            0
        )

        globalObject.setProperty(
            "kotlin",
            databind.conversionUtils.toJavascript(context, UltralightJsKotlin),
            0
        )

        globalObject.setProperty(
            "utils",
            databind.conversionUtils.toJavascript(context, UltralightJsUtils),
            0
        )

        if (view is ScreenView) {
            globalObject.setProperty(
                "screen",
                databind.conversionUtils.toJavascript(context, view.adaptedScreen ?: view.screen),
                0
            )

            val parentScreen = view.parentScreen

            if (parentScreen != null) {
                globalObject.setProperty(
                    "parentScreen",
                    databind.conversionUtils.toJavascript(context, view.parentScreen),
                    0
                )
            }
        }
    }
}