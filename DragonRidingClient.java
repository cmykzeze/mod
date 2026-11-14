package com.dragonriding.client;

import com.dragonriding.DragonInputPayload;
import com.dragonriding.DragonRiding;
import com.dragonriding.entity.RideableDragon;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.player.LocalPlayer;

public class DragonRidingClient implements ClientModInitializer {
    private static boolean sent = false;
    private static boolean lastFire, lastBoost, lastUp;

    @Override
    public void onInitializeClient() {
        ModelLayerRegistry.registerModelLayer(DragonModel.LAYER, DragonModel::createBodyLayer);
        EntityRendererRegistry.register(DragonRiding.DRAGON, DragonRenderer::new);

        // Controls (vanilla keys):  Attack/left-click = fire,  Sprint = boost,  Jump = rise
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            LocalPlayer player = client.player;
            if (player == null) {
                sent = false;
                return;
            }
            if (player.getVehicle() instanceof RideableDragon dragon && dragon.getControllingPassenger() == player) {
                boolean fire = client.screen == null && client.options.keyAttack.isDown();
                boolean boost = client.options.keySprint.isDown();
                boolean up = client.options.keyJump.isDown();
                dragon.setRiderInput(fire, boost, up);
                if (!sent || fire != lastFire || boost != lastBoost || up != lastUp) {
                    ClientPlayNetworking.send(new DragonInputPayload(fire, boost, up));
                    sent = true;
                    lastFire = fire;
                    lastBoost = boost;
                    lastUp = up;
                }
            } else {
                sent = false;
            }
        });
    }
}
