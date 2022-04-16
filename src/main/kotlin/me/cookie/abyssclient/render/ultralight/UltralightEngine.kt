package me.cookie.abyssclient.render.ultralight

import com.labymedia.ultralight.UltralightJava
import com.labymedia.ultralight.UltralightPlatform
import com.labymedia.ultralight.UltralightRenderer
import com.labymedia.ultralight.config.FontHinting
import com.labymedia.ultralight.config.UltralightConfig
import com.labymedia.ultralight.plugin.logging.UltralightLogLevel
import me.cookie.abyssclient.render.ultralight.fs.BrowserFileSystem
import me.cookie.abyssclient.render.ultralight.glfw.GlfwClipboardAdapter
import me.cookie.abyssclient.render.ultralight.glfw.GlfwCursorAdapter
import me.cookie.abyssclient.render.ultralight.glfw.GlfwInputAdapter
import me.cookie.abyssclient.render.ultralight.hooks.UltralightIntegrationHook
import me.cookie.abyssclient.render.ultralight.hooks.UltralightScreenHook
import me.cookie.abyssclient.render.ultralight.renderer.CpuViewRenderer
import me.cookie.abyssclient.utils.client.ThreadLock
import me.cookie.abyssclient.utils.client.logger
import me.cookie.abyssclient.utils.client.mc
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack

object UltralightEngine {
    var window = mc.window.handle
    private val res = UltralightResources()
    var platform = ThreadLock<UltralightPlatform>()
    var renderer = ThreadLock<UltralightRenderer>()

    /**
     * Glfw
     */
    lateinit var clipboardAdapter: GlfwClipboardAdapter
    lateinit var cursorAdapter: GlfwCursorAdapter
    lateinit var inputAdapter: GlfwInputAdapter


    /**
     * Views
     */
    val activeView: View?
        get() = views.find { it is ScreenView && mc.currentScreen == it.screen }

    private val views = mutableListOf<View>()

    fun init() {
        val refreshRate = mc.window.refreshRate

        logger.info("Loading ultralight...")

        // Check resources
        logger.info("Checking resources...")
        res.downloadResources()

        // Load natives from native directory inside root folder
        logger.debug("Loading ultralight natives")
        UltralightJava.load(res.binRoot.toPath());
        //UltralightGPUDriverNativeUtil.load(res.binRoot.toPath());

        // Setup platform
        logger.debug("Setting up ultralight platform")
        platform.lock(UltralightPlatform.instance())
        platform.get().setConfig(
            UltralightConfig().animationTimerDelay(1.0 / refreshRate).scrollTimerDelay(1.0 / refreshRate)
                .resourcePath(res.resourcesRoot.absolutePath).cachePath(res.cacheRoot.absolutePath)
                .fontHinting(FontHinting.SMOOTH)
        )
        platform.get().usePlatformFontLoader()
        platform.get().setFileSystem(BrowserFileSystem())
        platform.get().setClipboard(GlfwClipboardAdapter())
        platform.get().setLogger { level, message ->
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA") when (level) {
                UltralightLogLevel.ERROR -> logger.debug("[Ultralight/ERR] $message")
                UltralightLogLevel.WARNING -> logger.debug("[Ultralight/WARN] $message")
                UltralightLogLevel.INFO -> logger.debug("[Ultralight/INFO] $message")
            }
        }

        // Setup renderer
        logger.debug("Setting up ultralight renderer")
        renderer.lock(UltralightRenderer.create())

        // Setup hooks
        UltralightIntegrationHook
        UltralightScreenHook

        // Setup GLFW adapters
        clipboardAdapter = GlfwClipboardAdapter()
        cursorAdapter = GlfwCursorAdapter()
        inputAdapter = GlfwInputAdapter()

        logger.info("Successfully loaded ultralight!")
    }

    fun shutdown() {
        cursorAdapter.cleanup()
    }

    fun update() {
        views.forEach(View::update)
        renderer.get().update()
    }

    fun render(layer: RenderLayer, matrices: MatrixStack) {
        renderer.get().render()

        views.filter { it.layer == layer }.forEach {
            it.render(matrices)
        }
    }

    fun resize(width: Long, height: Long) {
        views.forEach { it.resize(width, height) }
    }

    fun newSplashView() = View(RenderLayer.SPLASH_LAYER, newViewRenderer()).also { views += it }

    fun newOverlayView() = View(RenderLayer.OVERLAY_LAYER, newViewRenderer()).also { views += it }

    fun newScreenView(screen: Screen, adaptedScreen: Screen? = null, parentScreen: Screen? = null) =
        ScreenView(newViewRenderer(), screen, adaptedScreen, parentScreen).also { views += it }

    fun removeView(view: View) {
        view.free()
        views.remove(view)
    }

    private fun newViewRenderer() = CpuViewRenderer()
}

enum class RenderLayer {
    OVERLAY_LAYER, SCREEN_LAYER, SPLASH_LAYER
}