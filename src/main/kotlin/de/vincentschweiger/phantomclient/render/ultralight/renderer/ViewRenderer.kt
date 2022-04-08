package de.vincentschweiger.phantomclient.render.ultralight.renderer;

import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.config.UltralightViewConfig;
import net.minecraft.client.util.math.MatrixStack

/**
 * Render Views
 */
interface ViewRenderer {

    /**
     * Setup [viewConfig]
     */
    fun setupConfig(viewConfig: UltralightViewConfig)

    /**
     * Render view
     */
    fun render(view: UltralightView, matrices: MatrixStack)

    /**
     * Delete
     */
    fun delete()

}