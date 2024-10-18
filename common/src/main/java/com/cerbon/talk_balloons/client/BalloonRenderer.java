package com.cerbon.talk_balloons.client;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.mixin.accessor.GuiGraphicsAccessor;
import com.cerbon.talk_balloons.util.HistoricalData;
import com.cerbon.talk_balloons.util.TBConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public final class BalloonRenderer {
    private static final ResourceLocation BALLOON_TEXTURE = ResourceLocation.fromNamespaceAndPath(TBConstants.MOD_ID, "textures/gui/balloon.png");

    private static final int MIN_BALLOON_WIDTH = TalkBalloons.config.minBalloonWidth;
    private static final int MAX_BALLOON_WIDTH = TalkBalloons.config.maxBalloonWidth;

    private static final Minecraft client = Minecraft.getInstance();

    public static void renderBalloons(PoseStack poseStack, EntityRenderDispatcher entityRenderDispatcher, Font font, HistoricalData<String> messages, float playerHeight) {
        GuiGraphics guiGraphics = GuiGraphicsAccessor.getGuiGraphics(client, poseStack, client.renderBuffers().bufferSource());
        Quaternionf rotation = Axis.YP.rotationDegrees(toEulerXyzDegrees(entityRenderDispatcher.cameraOrientation()).y + 180);

        int balloonDistance = 0;
        int previousBalloonHeight = 0;
        for (int i = 0; i < messages.size(); i++) {
            poseStack.pushPose();

            poseStack.translate(0.0, playerHeight + TalkBalloons.config.balloonsHeightOffset, 0.0D);
            poseStack.mulPose(rotation);
            poseStack.scale(-0.025F, -0.025F, 0.025F);

            RenderSystem.enableDepthTest();
            RenderSystem.enablePolygonOffset();
            RenderSystem.polygonOffset(3.0F, 3.0F);

            String message = messages.get(i);
            List<FormattedCharSequence> dividedMessage = font.split(FormattedText.of(message), MAX_BALLOON_WIDTH);

            int greatestTextWidth = 0;
            for (FormattedCharSequence text : dividedMessage) {
                int textWidth = font.width(text);

                if (textWidth > greatestTextWidth)
                    greatestTextWidth = textWidth;
            }

            int balloonWidth = Mth.clamp(greatestTextWidth, MIN_BALLOON_WIDTH, MAX_BALLOON_WIDTH);
            int balloonHeight = dividedMessage.size();

            if (balloonWidth % 2 == 0) // Width should be odd to correctly center the arrow
                balloonWidth--;

            if (previousBalloonHeight != 0)
                balloonDistance += 9 * previousBalloonHeight + TalkBalloons.config.distanceBetweenBalloons;

            previousBalloonHeight = balloonHeight;

            int j = balloonHeight - 1;
            int baseX = balloonWidth / 2;
            int baseY = (-balloonHeight - j * 7) - j;

            // Left
            guiGraphics.blit(BALLOON_TEXTURE, -baseX - 2, baseY - balloonDistance, 5, 5, 0.0F, 0.0F, 5, 5, 32, 32); // TOP
            guiGraphics.blit(BALLOON_TEXTURE, -baseX - 2, baseY + 5 - balloonDistance, 5, balloonHeight + j * 8, 0.0F, 6.0F, 5, 1, 32, 32); // MID
            guiGraphics.blit(BALLOON_TEXTURE, -baseX - 2, 5 - balloonDistance, 5, 5, 0.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

            // Mid
            guiGraphics.blit(BALLOON_TEXTURE, -baseX + 3, baseY - balloonDistance, balloonWidth - 4, 5, 6.0F, 0.0F, 5, 5, 32, 32); // TOP
            guiGraphics.blit(BALLOON_TEXTURE, -baseX + 3, baseY + 5 - balloonDistance, balloonWidth - 4, balloonHeight + j * 8, 6.0F, 6.0F, 5, 1, 32, 32); // MID
            guiGraphics.blit(BALLOON_TEXTURE, -baseX + 3, 5 - balloonDistance, balloonWidth - 4, 5, 6.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

            // Right
            guiGraphics.blit(BALLOON_TEXTURE,  baseX - 2, baseY - balloonDistance, 5, 5, 12.0F, 0.0F, 5, 5, 32, 32); // TOP
            guiGraphics.blit(BALLOON_TEXTURE,  baseX - 2, baseY + 5 - balloonDistance, 5, balloonHeight + j * 8, 12.0F, 6.0F, 5, 1, 32, 32); // MID
            guiGraphics.blit(BALLOON_TEXTURE,  baseX - 2, 5 - balloonDistance, 5, 5, 12.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

            RenderSystem.polygonOffset(0.0F, 0.0F);
            RenderSystem.disablePolygonOffset();

            // Arrow
            guiGraphics.blit(BALLOON_TEXTURE, -3, 9, 18, 6, 7, 4, 32, 32);

            if (dividedMessage.size() > 1) {
                int textDistance = 0;

                for (FormattedCharSequence text : dividedMessage) {
                    guiGraphics.drawString(font, text, -font.width(text) / 2 + 1, -(9 * balloonHeight - 10) - balloonDistance + textDistance, TalkBalloons.config.textColor, false);
                    textDistance += 9;
                }
            }
            else guiGraphics.drawString(font, message, -greatestTextWidth / 2 + 1, balloonHeight - balloonDistance, TalkBalloons.config.textColor, false);

            poseStack.popPose();
        }
    }

    private static Vector3f toEulerXyz(Quaternionf quaternionf) {
        float f = quaternionf.w() * quaternionf.w();
        float g = quaternionf.x() * quaternionf.x();
        float h = quaternionf.y() * quaternionf.y();
        float i = quaternionf.z() * quaternionf.z();
        float j = f + g + h + i;
        float k = 2.0f * quaternionf.w() * quaternionf.x() - 2.0f * quaternionf.y() * quaternionf.z();
        float l = (float) Math.asin(k / j);

        if (Math.abs(k) > 0.999f * j)
            return new Vector3f(l, 2.0f * (float) Math.atan2(quaternionf.y(), quaternionf.w()), 0.0f);

        return new Vector3f(l, (float) Math.atan2(2.0f * quaternionf.x() * quaternionf.z() + 2.0f * quaternionf.y() * quaternionf.w(), f - g - h + i), (float) Math.atan2(2.0f * quaternionf.x() * quaternionf.y() + 2.0f * quaternionf.w() * quaternionf.z(), f - g + h - i));
    }

    private static Vector3f toEulerXyzDegrees(Quaternionf quaternionf) {
        Vector3f vec3f = BalloonRenderer.toEulerXyz(quaternionf);
        return new Vector3f((float) Math.toDegrees(vec3f.x()), (float) Math.toDegrees(vec3f.y()), (float) Math.toDegrees(vec3f.z()));
    }
}
