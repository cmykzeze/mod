package com.dragonriding.client;

import com.dragonriding.DragonRiding;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class DragonRidingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(DragonModel.LAYER, DragonModel::createBodyLayer);
        EntityRendererRegistry.register(DragonRiding.DRAGON, DragonRenderer::new);
    }
}
