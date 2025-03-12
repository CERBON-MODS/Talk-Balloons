package com.cerbon.talk_balloons.client;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.util.HistoricalData;
import com.cerbon.talk_balloons.util.TBConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

@Environment(EnvType.CLIENT)
public final class BalloonRenderer {
    private static final ResourceLocation BALLOON_TEXTURE = new ResourceLocation(TBConstants.MOD_ID, "textures/gui/balloon.png");

    private static final int MIN_BALLOON_WIDTH = TalkBalloons.config.minBalloonWidth;
    private static final int MAX_BALLOON_WIDTH = TalkBalloons.config.maxBalloonWidth;

    private static final Minecraft client = Minecraft.getInstance();

    public static void renderBalloons(PoseStack poseStack, EntityRenderDispatcher entityRenderDispatcher, Font font, HistoricalData<String> messages, float playerHeight) {
        Quaternion rotation = Vector3f.YP.rotationDegrees(toEulerXyzDegrees(entityRenderDispatcher.cameraOrientation()).y());

        int balloonDistance = 0;
        int previousBalloonHeight = 0;
        for (int i = 0; i < messages.size(); i++) {
            poseStack.pushPose();

            poseStack.translate(0.0, playerHeight + TalkBalloons.config.balloonsHeightOffset, 0.0D);
            poseStack.mulPose(rotation);
            poseStack.scale(-0.025F, -0.025F, 0.025F);

            RenderSystem.enableBlend();
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
            RenderSystem.setShaderTexture(0, BALLOON_TEXTURE);
            Screen.blit(poseStack, -baseX - 2, baseY - balloonDistance, 5, 5, 0.0F, 0.0F, 5, 5, 32, 32); // TOP
            Screen.blit(poseStack, -baseX - 2, baseY + 5 - balloonDistance, 5, balloonHeight + j * 8, 0.0F, 6.0F, 5, 1, 32, 32); // MID
            Screen.blit(poseStack, -baseX - 2, 5 - balloonDistance, 5, 5, 0.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

            // Mid
            Screen.blit(poseStack, -baseX + 3, baseY - balloonDistance, balloonWidth - 4, 5, 6.0F, 0.0F, 5, 5, 32, 32); // TOP
            Screen.blit(poseStack, -baseX + 3, baseY + 5 - balloonDistance, balloonWidth - 4, balloonHeight + j * 8, 6.0F, 6.0F, 5, 1, 32, 32); // MID
            Screen.blit(poseStack, -baseX + 3, 5 - balloonDistance, balloonWidth - 4, 5, 6.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

            // Right
            Screen.blit(poseStack,  baseX - 2, baseY - balloonDistance, 5, 5, 12.0F, 0.0F, 5, 5, 32, 32); // TOP
            Screen.blit(poseStack,  baseX - 2, baseY + 5 - balloonDistance, 5, balloonHeight + j * 8, 12.0F, 6.0F, 5, 1, 32, 32); // MID
            Screen.blit(poseStack,  baseX - 2, 5 - balloonDistance, 5, 5, 12.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

            RenderSystem.polygonOffset(0.0F, 0.0F);
            RenderSystem.disablePolygonOffset();

            // Arrow
            Screen.blit(poseStack, -3, 9, 18, 6, 7, 4, 32, 32);
            RenderSystem.disableBlend();

            if (dividedMessage.size() > 1) {
                int textDistance = 0;

                for (FormattedCharSequence text : dividedMessage) {
                    font.draw(poseStack, text, -font.width(text) / 2 + 1, -(9 * balloonHeight - 10) - balloonDistance + textDistance, TalkBalloons.config.textColor);
                    textDistance += 9;
                }
            }
            else font.draw(poseStack, message, -greatestTextWidth / 2 + 1, balloonHeight - balloonDistance, TalkBalloons.config.textColor);

            poseStack.popPose();
        }
    }

    private static Vector3f toEulerXyz(Quaternion quaternionf) {
        float f = quaternionf.r() * quaternionf.r(); // w
        float g = quaternionf.i() * quaternionf.i(); // x
        float h = quaternionf.j() * quaternionf.j(); // y
        float i = quaternionf.k() * quaternionf.k(); // z
        float j = f + g + h + i;
        float k = 2.0f * quaternionf.r() * quaternionf.i() - 2.0f * quaternionf.j() * quaternionf.k();
        float l = (float) Math.asin(k / j);

        if (Math.abs(k) > 0.999f * j)
            return new Vector3f(l, 2.0f * (float) Math.atan2(quaternionf.j(), quaternionf.r()), 0.0f);

        return new Vector3f(l, (float) Math.atan2(2.0f * quaternionf.i() * quaternionf.k() + 2.0f * quaternionf.j() * quaternionf.r(), f - g - h + i), (float) Math.atan2(2.0f * quaternionf.i() * quaternionf.j() + 2.0f * quaternionf.r() * quaternionf.k(), f - g + h - i));
    }

    private static Vector3f toEulerXyzDegrees(Quaternion quaternionf) {
        Vector3f vec3f = BalloonRenderer.toEulerXyz(quaternionf);
        return new Vector3f((float) Math.toDegrees(vec3f.x()), (float) Math.toDegrees(vec3f.y()), (float) Math.toDegrees(vec3f.z()));
    }
}
