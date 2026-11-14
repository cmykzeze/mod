package com.dragonriding;

import com.dragonriding.entity.RideableDragon;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class DragonRiding implements ModInitializer {
    public static final String MOD_ID = "dragonriding";

    public static final ResourceKey<EntityType<?>> DRAGON_KEY = ResourceKey.create(
            Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "rideable_dragon"));

    public static final EntityType<RideableDragon> DRAGON = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, DRAGON_KEY,
            EntityType.Builder.of(RideableDragon::new, MobCategory.CREATURE)
                    .sized(2.5f, 2.0f)
                    .clientTrackingRange(10)
                    .build(DRAGON_KEY));

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(DRAGON, RideableDragon.createAttributes());
    }
}
