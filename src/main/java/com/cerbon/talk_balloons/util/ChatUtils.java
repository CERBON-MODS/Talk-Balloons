package com.cerbon.talk_balloons.util;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.client.TalkBalloonsClient;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
//? if >= 1.19.2 {
import net.minecraft.network.chat.contents.TranslatableContents;
//?} else {
/*import net.minecraft.network.chat.TranslatableComponent;
*///?}
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ChatUtils {
    public record MessageContents(UUID sender, String contents) {}

    public static @Nullable MessageContents tryExtractContents(Component component) {
        if (
            //? if >= 1.19.2 {
            component.getContents() instanceof TranslatableContents contents
            //?} else {
            /*component instanceof TranslatableComponent contents
            *///?}
            && contents.getKey().equals("chat.type.text") && contents.getArgs().length >= 2) {
            var arg = contents.getArgs()[0];
            var arg2 = contents.getArgs()[1];

            var message = arg2 instanceof Component component1 ? component1.getString() : arg2.toString();

            if (arg instanceof MutableComponent mutableComponent && mutableComponent.getStyle().getHoverEvent() != null
                && mutableComponent.getStyle().getHoverEvent()/*? if <= 1.21.4 {*/.getAction()/*?} else {*//*.action()*//*?}*/ != HoverEvent.Action.SHOW_ENTITY
            ) {
                //? if <= 1.21.4 {
                var value = mutableComponent.getStyle().getHoverEvent().getValue(HoverEvent.Action.SHOW_ENTITY);
                //?} else {
                /*var value = ((HoverEvent.ShowEntity) mutableComponent.getStyle().getHoverEvent()).entity();
                 *///?}

                if (value != null && value.type == EntityType.PLAYER) {
                    UUID uuid = value/*? if <= 1.21.4 {*/.id/*?} else {*//*.uuid*//*?}*/;
                    return new MessageContents(uuid, message);
                }
            }
        }

        // Modified from Talk Bubbles, licensed under MIT
        // https://github.com/Globox1997/TalkBubbles/blob/1.21/src/main/java/net/talkbubbles/mixin/ChatHudMixin.java#L104
        String sender = null;
        UUID senderUUID = null;

        String[] words = component.getString().split("(§.)|[^\\w§]+");
        String[] parts = component.toString().split("key='");

        if (parts.length > 1) {
            String translationKey = parts[1].split("'")[0];
            if (translationKey.contains("commands")) {
                return null;
            } else if (translationKey.contains("advancement")) {
                return null;
            }
        }

        words: for (String word : words) {
            if (word.isEmpty())
                continue;

            //? if >= 1.19.2 {
            UUID possibleUUID = Minecraft.getInstance().getPlayerSocialManager().getDiscoveredUUID(word);
            if (possibleUUID != Util.NIL_UUID) {
                sender = word;
                senderUUID = possibleUUID;
                break;
            }
            //?} else {
            /*ClientLevel level = Minecraft.getInstance().level;

            if (level != null) {
                for (AbstractClientPlayer player : level.players()) {
                    if (player.getGameProfile().getName().equalsIgnoreCase(word)) {
                        sender = word;
                        senderUUID = player.getUUID();

                        break words;
                    }
                }
            }
            *///?}
        }

        if (sender == null)
            return null;

        String message = component.getString().replaceFirst("[\\s\\S]*" + sender + "([^\\p{L}§]|(§.)?)+\\s+", "");
        return new MessageContents(senderUUID, message);
    }
}
