package com.cerbon.talk_balloons.client;

import com.cerbon.talk_balloons.mixin.accessor.GuiGraphicsAccessor;
import com.cerbon.talk_balloons.util.TBConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BalloonRenderer {
    private static final ResourceLocation BALLOON_TEXTURE = new ResourceLocation(TBConstants.MOD_ID, "textures/gui/balloon.png");

    public static void renderBalloon(PoseStack poseStack, MultiBufferSource buffer, EntityRenderDispatcher entityRenderDispatcher, Font font, List<String> textList, int balloonWidth, int balloonHeight, float playerHeight, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(0.0, playerHeight + 0.6F, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(toEulerXyzDegrees(entityRenderDispatcher.cameraOrientation()).y));
        poseStack.scale(-0.025F, -0.025F, 0.025F);

        Matrix4f matrix4f = poseStack.last().pose();

        RenderSystem.enableDepthTest();
        RenderSystem.enablePolygonOffset();
        RenderSystem.polygonOffset(3.0F, 3.0F);

        Minecraft client = Minecraft.getInstance();
        GuiGraphics guiGraphics = GuiGraphicsAccessor.getGuiGraphics(client, poseStack, client.renderBuffers().bufferSource());

        // Left
        guiGraphics.blit(BALLOON_TEXTURE, -balloonWidth / 2 - 2, -balloonHeight - (balloonHeight - 1) * 7, 5, 5, 0.0F, 0.0F, 5, 5, 32, 32); // TOP
        guiGraphics.blit(BALLOON_TEXTURE, -balloonWidth / 2 - 2, -balloonHeight - (balloonHeight - 1) * 7 + 5, 5, balloonHeight + (balloonHeight - 1) * 8, 0.0F, 6.0F, 5, 1, 32, 32); // MID
        guiGraphics.blit(BALLOON_TEXTURE, -balloonWidth / 2 - 2, 5 + (balloonHeight - 1), 5, 5, 0.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

        // Mid
        guiGraphics.blit(BALLOON_TEXTURE, -balloonWidth / 2 + 3, -balloonHeight - (balloonHeight - 1) * 7, balloonWidth - 4, 5, 6.0F, 0.0F, 5, 5, 32, 32); // TOP
        guiGraphics.blit(BALLOON_TEXTURE, -balloonWidth / 2 + 3, -balloonHeight - (balloonHeight - 1) * 7 + 5, balloonWidth - 4, balloonHeight + (balloonHeight - 1) * 8, 6.0F, 6.0F, 5, 1, 32, 32); // MID
        guiGraphics.blit(BALLOON_TEXTURE, -balloonWidth / 2 + 3, 5 + (balloonHeight - 1), balloonWidth - 4, 5, 6.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

        // Right
        guiGraphics.blit(BALLOON_TEXTURE, balloonWidth / 2 - 2, -balloonHeight - (balloonHeight - 1) * 7, 5, 5, 12.0F, 0.0F, 5, 5, 32, 32); // TOP
        guiGraphics.blit(BALLOON_TEXTURE, balloonWidth / 2 - 2, -balloonHeight - (balloonHeight - 1) * 7 + 5, 5, balloonHeight + (balloonHeight - 1) * 8, 12.0F, 6.0F, 5, 1, 32, 32); // MID
        guiGraphics.blit(BALLOON_TEXTURE, balloonWidth / 2 - 2, 5 + (balloonHeight - 1), 5, 5, 12.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

        // Arrow
        RenderSystem.polygonOffset(2.0F, 2.0F);
        guiGraphics.blit(BALLOON_TEXTURE, -3, 9, 18, 6, 7, 4, 32, 32);

        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();

        poseStack.popPose();
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
