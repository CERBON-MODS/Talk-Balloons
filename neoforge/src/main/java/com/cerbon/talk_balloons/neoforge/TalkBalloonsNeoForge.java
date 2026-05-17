package com.cerbon.talk_balloons.neoforge;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.compat.CompatHandler;
import com.cerbon.talk_balloons.neoforge.event.TBClientEventsNeoForge;
import com.cerbon.talk_balloons.neoforge.event.TBServerEventsNeoForge;
import com.cerbon.talk_balloons.network.TBClientPacketHandler;
import com.cerbon.talk_balloons.network.TBPackets;
import com.cerbon.talk_balloons.network.TBServerPacketHandler;
import com.cerbon.talk_balloons.util.TBConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;

@Mod(TBConstants.MOD_ID)
public class TalkBalloonsNeoForge {
    public TalkBalloonsNeoForge(IEventBus bus) {
        TalkBalloons.init();
        CompatHandler.isIrisLoaded = isModLoaded("iris") || isModLoaded("oculus");

        if (
            //? if < 1.21.10 {
            FMLLoader.getDist()
            //? } else {
            /*FMLLoader.getCurrent().getDist()
            *///? }
                == Dist.CLIENT
        ) {
            NeoForge.EVENT_BUS.register(TBClientEventsNeoForge.TBNeoForgeClientEvents.class);
            bus.register(TBClientEventsNeoForge.class);
        }

        NeoForge.EVENT_BUS.register(TBServerEventsNeoForge.class);
        bus.register(TalkBalloonsNeoForge.class);
    }

    private boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId) || LoadingModList.get().getModFileById(modId) != null;
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("" + TBPackets.PROTOCOL_VERSION);
        registrar.playBidirectional(TBPackets.STATUS.type(), TBPackets.STATUS.codec(), (packet, ctx) -> {
            if (ctx.flow() == PacketFlow.CLIENTBOUND) {
                TBClientPacketHandler.handleStatus(packet);
            } else {
                TBServerPacketHandler.handleStatus((ServerPlayer) ctx.player(), packet);
            }
        });

        registrar.playToServer(TBPackets.SYNC_BALLOON_CONFIG.type(), TBPackets.SYNC_BALLOON_CONFIG.codec(), (packet, ctx) -> {
            TBServerPacketHandler.handleSyncBalloonConfig((ServerPlayer) ctx.player(), packet);
        });

        registrar.playToClient(TBPackets.CREATE_BALLOON.type(), TBPackets.CREATE_BALLOON.codec(), (packet, ctx) -> {
            TBClientPacketHandler.handleCreateBalloon(packet);
        });

        registrar.playToClient(TBPackets.SYNC_CONFIG_TO_PLAYER.type(), TBPackets.SYNC_CONFIG_TO_PLAYER.codec(), (packet, ctx) -> {
            TBClientPacketHandler.handleSyncConfigToPlayer(packet);
        });
    }
}
