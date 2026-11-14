package com.dragonriding.client;

import com.dragonriding.DragonRiding;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class DragonRidingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLayerRegistry.registerModelLayer(DragonModel.LAYER, DragonModel::createBodyLayer);
        EntityRendererRegistry.register(DragonRiding.DRAGON, DragonRenderer::new);
    }
}
