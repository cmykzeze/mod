package com.dragonriding;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** Sent client -> server while riding: which dragon controls are held. */
public record DragonInputPayload(boolean fire, boolean boost, boolean up) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DragonInputPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(DragonRiding.MOD_ID, "dragon_input"));

    public static final StreamCodec<ByteBuf, DragonInputPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, DragonInputPayload::fire,
            ByteBufCodecs.BOOL, DragonInputPayload::boost,
            ByteBufCodecs.BOOL, DragonInputPayload::up,
            DragonInputPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
