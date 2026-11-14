package com.dragonriding;

import com.dragonriding.entity.RideableDragon;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

public class DragonRiding implements ModInitializer {
    public static final String MOD_ID = "dragonriding";

    public static final ResourceKey<EntityType<?>> DRAGON_KEY = ResourceKey.create(
            Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "rideable_dragon"));

    public static final EntityType<RideableDragon> DRAGON = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, DRAGON_KEY,
            EntityType.Builder.of(RideableDragon::new, MobCategory.CREATURE)
                    .sized(3.0f, 2.4f)
                    .clientTrackingRange(12)
                    .build(DRAGON_KEY));

    public static final ResourceKey<Item> SPAWN_EGG_KEY = ResourceKey.create(
            Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "dragon_spawn_egg"));

    public static final Item DRAGON_SPAWN_EGG = Registry.register(
            BuiltInRegistries.ITEM, SPAWN_EGG_KEY,
            new SpawnEggItem(new Item.Properties().setId(SPAWN_EGG_KEY).spawnEgg(DRAGON)));

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(DRAGON, RideableDragon.createAttributes());

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS)
                .register(tab -> tab.accept(DRAGON_SPAWN_EGG));

        // client -> server: held controls while riding (fire / boost / rise)
        PayloadTypeRegistry.serverboundPlay().register(DragonInputPayload.TYPE, DragonInputPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(DragonInputPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            if (player.getVehicle() instanceof RideableDragon dragon) {
                dragon.setRiderInput(payload.fire(), payload.boost(), payload.up());
            }
        });

        // only on End stone (never in the void)
        SpawnPlacements.register(DRAGON, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, reason, pos, random) -> level.getBlockState(pos.below()).is(Blocks.END_STONE));

        // natural spawning in the End outer islands
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS,
                        Biomes.END_BARRENS, Biomes.SMALL_END_ISLANDS),
                MobCategory.CREATURE, DRAGON, 6, 1, 2);
    }
}
