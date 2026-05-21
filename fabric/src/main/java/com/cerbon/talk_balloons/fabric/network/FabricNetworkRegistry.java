package com.cerbon.talk_balloons.fabric.network;

import com.cerbon.talk_balloons.network.TBPackets;

import net.minecraft.network.RegistryFriendlyByteBuf;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class FabricNetworkRegistry {
    public static void init() {
        playClientbound().register(TBPackets.STATUS.type(), TBPackets.STATUS.codec());
        playServerbound().register(TBPackets.STATUS.type(), TBPackets.STATUS.codec());

        playServerbound().register(TBPackets.SYNC_BALLOON_CONFIG.type(), TBPackets.SYNC_BALLOON_CONFIG.codec());
        playClientbound().register(TBPackets.CREATE_BALLOON.type(), TBPackets.CREATE_BALLOON.codec());
        playClientbound().register(TBPackets.SYNC_CONFIG_TO_PLAYER.type(), TBPackets.SYNC_CONFIG_TO_PLAYER.codec());
    }

    private static PayloadTypeRegistry<RegistryFriendlyByteBuf> playClientbound() {
        return
            //? if < 26.1 {
            PayloadTypeRegistry.playS2C()
            //? } else {
            /*PayloadTypeRegistry.clientboundPlay()
             *///? }
        ;
    }

    private static PayloadTypeRegistry<RegistryFriendlyByteBuf> playServerbound() {
        return
            //? if < 26.1 {
            PayloadTypeRegistry.playC2S()
            //? } else {
            /*PayloadTypeRegistry.serverboundPlay()
             *///? }
            ;
    }
}
