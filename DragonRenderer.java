package com.dragonriding.client;

import com.dragonriding.DragonRiding;
import com.dragonriding.entity.RideableDragon;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class DragonRenderer extends MobRenderer<RideableDragon, DragonRenderState, DragonModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(DragonRiding.MOD_ID, "textures/entity/rideable_dragon.png");

    public DragonRenderer(EntityRendererProvider.Context context) {
        super(context, new DragonModel(context.bakeLayer(DragonModel.LAYER)), 1.2F);
    }

    @Override
    public DragonRenderState createRenderState() {
        return new DragonRenderState();
    }

    @Override
    public Identifier getTextureLocation(DragonRenderState state) {
        return TEXTURE;
    }
}
