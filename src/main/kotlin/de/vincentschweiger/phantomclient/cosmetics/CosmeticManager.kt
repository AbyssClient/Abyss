package de.vincentschweiger.phantomclient.cosmetics

import de.vincentschweiger.phantomclient.cosmetics.hat.HatModel
import de.vincentschweiger.phantomclient.cosmetics.hat.HatRenderer
import de.vincentschweiger.phantomclient.cosmetics.wings.dragonwings.DragonWingsModel
import de.vincentschweiger.phantomclient.cosmetics.wings.dragonwings.DragonWingsRenderer
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry

object CosmeticManager {
    init {
        EntityModelLayerRegistry.registerModelLayer(HatRenderer.LAYER) { HatModel.texturedModelData }
        EntityModelLayerRegistry.registerModelLayer(DragonWingsRenderer.LAYER) { DragonWingsModel.texturedModelData }
    }
}