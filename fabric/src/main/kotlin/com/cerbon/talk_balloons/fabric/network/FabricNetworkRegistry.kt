package com.cerbon.talk_balloons.fabric.network

import com.cerbon.talk_balloons.network.TBPackets
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

object FabricNetworkRegistry {
    @JvmStatic
    fun init() {
        playClientbound().register(TBPackets.STATUS)
        playServerbound().register(TBPackets.STATUS)

        playServerbound().register(TBPackets.SYNC_BALLOON_CONFIG)
        playClientbound().register(TBPackets.CREATE_BALLOON)
        playClientbound().register(TBPackets.SYNC_CONFIG_TO_PLAYER)
    }

    private fun playClientbound(): PayloadTypeRegistry<RegistryFriendlyByteBuf> =
        //? if < 26.1 {
        PayloadTypeRegistry.playS2C()
        //? } else {
        /*PayloadTypeRegistry.clientboundPlay()
        *///? }

    private fun playServerbound(): PayloadTypeRegistry<RegistryFriendlyByteBuf> =
        //? if < 26.1 {
        PayloadTypeRegistry.playC2S()
        //? } else {
        /*PayloadTypeRegistry.serverboundPlay()
        *///? }

    private fun <B : FriendlyByteBuf, T : CustomPacketPayload> PayloadTypeRegistry<B>.register(combined: CustomPacketPayload.TypeAndCodec<in B, T>) {
        register(combined.type, combined.codec)
    }
}
