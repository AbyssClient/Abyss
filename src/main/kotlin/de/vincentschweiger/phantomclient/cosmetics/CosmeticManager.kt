package de.vincentschweiger.phantomclient.cosmetics

import de.vincentschweiger.phantomclient.cosmetics.dragonwings.DragonwingsModel
import de.vincentschweiger.phantomclient.cosmetics.dragonwings.DragonwingsRenderer
import de.vincentschweiger.phantomclient.cosmetics.hat.HatModel
import de.vincentschweiger.phantomclient.cosmetics.wings.dragonwings.DragonWingsModel
import de.vincentschweiger.phantomclient.cosmetics.wings.dragonwings.DragonWingsRenderer
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry

object CosmeticManager {
    init {
        EntityModelLayerRegistry.registerModelLayer(de.vincentschweiger.phantomclient.cosmetics.hat.LAYER) { HatModel.texturedModelData }
        EntityModelLayerRegistry.registerModelLayer(DragonWingsRenderer.LAYER) { DragonWingsModel.texturedModelData }
    }
}