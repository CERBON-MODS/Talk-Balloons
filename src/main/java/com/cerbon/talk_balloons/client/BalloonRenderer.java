package com.cerbon.talk_balloons.client;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.mixin.GuiGraphicsAccessor;
import com.cerbon.talk_balloons.util.HistoricalData;
import com.cerbon.talk_balloons.util.SynchronizedConfigData;
import com.cerbon.talk_balloons.util.TBConstants;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
//? if >= 1.20 {
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Quaternionf;
import org.joml.Vector3f;
//?} else {
/*import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
*///?}

import java.util.List;

@Environment(EnvType.CLIENT)
public final class BalloonRenderer {
    private static final Minecraft client = Minecraft.getInstance();

    public static void renderBalloons(PoseStack poseStack, EntityRenderDispatcher entityRenderDispatcher, Font font, HistoricalData<Component> messages, float playerHeight, SynchronizedConfigData configData) {
        //? if >= 1.20 {
        Quaternionf rotation = Axis.YP.rotationDegrees(toEulerXyzDegrees(entityRenderDispatcher.cameraOrientation()).y /*? if >= 1.21.1 {*//* + 180f*//*?}*/);
        //?} else {
        /*var rotation = Vector3f.YP.rotationDegrees(toEulerXyzDegrees(entityRenderDispatcher.cameraOrientation()).y());
        *///?}

        int balloonDistance = 0;
        int previousBalloonHeight = 0;
        int padding = configData.balloonPadding();
        ResourceLocation balloonTexture = configData.balloonStyle().getTextureId();

        var r = (configData.balloonTint() >> 16) & 255;
        var g = (configData.balloonTint() >> 8) & 255;
        var b = configData.balloonTint() & 255;

        for (int i = 0; i < messages.size(); i++) {
            Component message = messages.get(i);
            poseStack.pushPose();

            poseStack.translate(0.0, playerHeight + TalkBalloons.config.balloonsHeightOffset - 0.4 + (padding / 32.0), 0.0D);
            poseStack.mulPose(rotation);
            poseStack.scale(-0.025F, -0.025F, 0.025F);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.enablePolygonOffset();
            RenderSystem.polygonOffset(3.0F, 3.0F);
            RenderSystem.setShaderColor(r / 255f, g / 255f, b / 255f, 1f);

            List<FormattedCharSequence> dividedMessage = font.split(message, TalkBalloons.config.maxBalloonWidth);

            int greatestTextWidth = 0;
            for (FormattedCharSequence text : dividedMessage) {
                int textWidth = font.width(text);

                if (textWidth > greatestTextWidth)
                    greatestTextWidth = textWidth;
            }

            int balloonWidth = Mth.clamp(greatestTextWidth, TalkBalloons.config.minBalloonWidth, TalkBalloons.config.maxBalloonWidth);
            int balloonHeight = dividedMessage.size();

            if (balloonWidth % 2 == 0) // Width should be odd to correctly center the arrow
                balloonWidth--;

            if (previousBalloonHeight != 0)
                balloonDistance += 9 * previousBalloonHeight + TalkBalloons.config.distanceBetweenBalloons + (padding * 2);

            previousBalloonHeight = balloonHeight;

            int j = balloonHeight - 1;
            int baseX = balloonWidth / 2;
            int baseY = (-balloonHeight - j * 7) - j;

            // Left
            blit(poseStack, balloonTexture, -baseX - 3 - padding, baseY - balloonDistance - padding, 5, 5, 0.0F, 0.0F, 5, 5, 32, 32); // TOP
            blit(poseStack, balloonTexture, -baseX - 3 - padding, baseY + 5 - balloonDistance - padding, 5, balloonHeight + j * 8 + (padding * 2), 0.0F, 6.0F, 5, 1, 32, 32); // MID
            blit(poseStack, balloonTexture, -baseX - 3 - padding, 5 - balloonDistance + padding, 5, 5, 0.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

            // Mid
            blit(poseStack, balloonTexture, -baseX + 2 - padding, baseY - balloonDistance - padding, balloonWidth - 4 + (padding * 2), 5, 6.0F, 0.0F, 5, 5, 32, 32); // TOP
            blit(poseStack, balloonTexture, -baseX + 2 - padding, baseY + 5 - balloonDistance - padding, balloonWidth - 4 + (padding * 2), balloonHeight + j * 8 + (padding * 2), 6.0F, 6.0F, 5, 1, 32, 32); // MID
            blit(poseStack, balloonTexture, -baseX + 2 - padding, 5 - balloonDistance + padding, balloonWidth - 4 + (padding * 2), 5, 6.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

            // Right
            blit(poseStack, balloonTexture, baseX - 1 + padding, baseY - balloonDistance - padding, 5, 5, 12.0F, 0.0F, 5, 5, 32, 32); // TOP
            blit(poseStack, balloonTexture, baseX - 1 + padding, baseY + 5 - balloonDistance - padding, 5, balloonHeight + j * 8 + (padding * 2), 12.0F, 6.0F, 5, 1, 32, 32); // MID
            blit(poseStack, balloonTexture, baseX - 1 + padding, 5 - balloonDistance + padding, 5, 5, 12.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

            RenderSystem.polygonOffset(0.0F, 0.0F);
            RenderSystem.disablePolygonOffset();

            // Arrow
            if (i == 0)
                blit(poseStack, balloonTexture, -3, 9 + padding, 7, 4, 18, 6, 7, 4, 32, 32);

            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

            if (dividedMessage.size() > 1) {
                int textDistance = 0;

                for (FormattedCharSequence text : dividedMessage) {
                    drawString(poseStack, font, text, -font.width(text) / 2 + 1, -(9 * balloonHeight - 10) - balloonDistance + textDistance, configData.textColor(), false);
                    textDistance += 9;
                }
            } else
                drawString(poseStack, font, message.getVisualOrderText(), -greatestTextWidth / 2 + 1, balloonHeight - balloonDistance, configData.textColor(), false);

            poseStack.popPose();
        }
    }

    private static void drawString(PoseStack poseStack, Font font, FormattedCharSequence text, int x, int y, int color, boolean dropShadow) {
        //? if >= 1.20 {
        font.drawInBatch(text, (float) x, (float) y, color, dropShadow, poseStack.last().pose(), client.renderBuffers().bufferSource(), Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
        //?} else {
        /*font.drawInBatch(text, (float) x, (float) y, color, dropShadow, poseStack.last().pose(), client.renderBuffers().bufferSource(), false, 0, LightTexture.FULL_BRIGHT);
        *///?}
    }

    //? if >= 1.20 {
    private static GuiGraphics currentContext;
    private static GuiGraphics getContext(PoseStack stack) {
        if (currentContext == null || currentContext.pose() != stack) {
            currentContext = new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
            ((GuiGraphicsAccessor) currentContext).setPose(stack);
        }

        return currentContext;
    }
    //?}

    private static void blit(PoseStack poseStack, ResourceLocation location, int x, int y, int width, int height, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        //? if < 1.20 {
        /*RenderSystem.setShaderTexture(0, location);
        Screen.blit(poseStack, x, y, width, height, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
        *///?} else {
        var guiGraphics = getContext(poseStack);
        //? if >= 1.21.3 {
        /*guiGraphics.blit(RenderType::guiTextured, location, x, y, uOffset, vOffset, width, height, uWidth, vHeight, textureWidth, textureHeight);
        *///?} else {
        guiGraphics.blit(location, x, y, width, height, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
        //?}
        //?}
    }

    private static Vector3f toEulerXyz(/*? if >= 1.20 {*/Quaternionf/*?} else {*//*Quaternion*//*?}*/ quaternionf) {
        //? if >= 1.20 {
        
        var w = quaternionf.w();
        var x = quaternionf.x();
        var y = quaternionf.y();
        var z = quaternionf.z();
        //?} else {
        /*// Mojang why
        var w = quaternionf.r();
        var x = quaternionf.i();
        var y = quaternionf.j();
        var z = quaternionf.k();
        *///?}

        float f = w * x;
        float g = x * x;
        float h = y * y;
        float i = z * z;
        float j = f + g + h + i;
        float k = 2.0f * w * x - 2.0f * y * z;
        float l = (float) Math.asin(k / j);

        if (Math.abs(k) > 0.999f * j)
            return new Vector3f(l, 2.0f * (float) Math.atan2(y, w), 0.0f);

        return new Vector3f(l, (float) Math.atan2(2.0f * x * z + 2.0f * y * w, f - g - h + i), (float) Math.atan2(2.0f * x * y + 2.0f * w * z, f - g + h - i));
    }

    private static Vector3f toEulerXyzDegrees(/*? if >= 1.20 {*/Quaternionf/*?} else {*//*Quaternion*//*?}*/ quaternionf) {
        Vector3f vec3f = BalloonRenderer.toEulerXyz(quaternionf);
        return new Vector3f((float) Math.toDegrees(vec3f.x()), (float) Math.toDegrees(vec3f.y()), (float) Math.toDegrees(vec3f.z()));
    }
}
