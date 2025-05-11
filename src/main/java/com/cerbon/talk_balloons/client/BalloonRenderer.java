package com.cerbon.talk_balloons.client;

import com.cerbon.talk_balloons.TalkBalloons;
import com.cerbon.talk_balloons.util.HistoricalData;
import com.cerbon.talk_balloons.util.SynchronizedConfigData;
import com.cerbon.talk_balloons.util.TBConstants;
//? if >= 1.21.5 {
/*import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderPass;
*///?}
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
//? if >= 1.20 {
import com.mojang.math.Axis;
import org.joml.Quaternionf;
import org.joml.Vector3f;
//?} else {
/*import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
*///?}

import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@Environment(EnvType.CLIENT)
public final class BalloonRenderer {
    private static final Minecraft client = Minecraft.getInstance();

    //? if >= 1.21.5 {
    /*private static final RenderPipeline.Snippet BALLOON_SNIPPET = RenderPipeline.builder()
        .withUniform("ModelViewMat", UniformType.MATRIX4X4) // Matrices snippet
        .withUniform("ProjMat", UniformType.MATRIX4X4)
        .withUniform("ColorModulator", UniformType.VEC4) // Matrices Color snippet
        .withVertexShader("core/position_tex_color") // guiTextured defaults
        .withFragmentShader("core/position_tex_color")
        .withSampler("Sampler0")
        .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
        .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST) // enableDepthTest
        .buildSnippet();

    private static final RenderPipeline MAIN_BALLOON_PIPELINE = RenderPipeline.builder(BALLOON_SNIPPET)
        .withLocation("pipeline/gui_textured")
        .withDepthBias(3.0f, 3.0f) // polygonOffset
        .withBlend(BlendFunction.PANORAMA) // enableBlend + defaultBlendFunc
        .build();

    private static final RenderPipeline BALLOON_ARROW_PIPELINE = RenderPipeline.builder(BALLOON_SNIPPET)
        .withLocation("pipeline/gui_textured")
        .withDepthBias(0.0f, 0.0f) // disablePolygonOffset
        .withBlend(BlendFunction.PANORAMA) // enableBlend + defaultBlendFunc
        .build();
    *///?}

    public static void renderBalloons(PoseStack poseStack, MultiBufferSource bufferSource, EntityRenderDispatcher entityRenderDispatcher, Font font, HistoricalData<Component> messages, float playerHeight, SynchronizedConfigData configData) {
        if (messages.isEmpty())
            return;

        //? if >= 1.20 {
        Quaternionf rotation = Axis.YP.rotationDegrees(toEulerXyzDegrees(entityRenderDispatcher.cameraOrientation()).y /*? if >= 1.21.1 {*/ /*+ 180f*//*?}*/);
        //?} else {
        /*var rotation = Vector3f.YP.rotationDegrees(toEulerXyzDegrees(entityRenderDispatcher.cameraOrientation()).y());
        *///?}

        int balloonDistance = 0;
        int previousBalloonHeight = 0;
        int padding = configData.balloonPadding();
        ResourceLocation balloonTexture = configData.balloonStyle().getTextureId();
        //? if >= 1.21.5 {
        /*var balloonGpuTexture = client.getTextureManager().getTexture(balloonTexture).getTexture();
        var renderTarget = client.getMainRenderTarget();
        var encoder = RenderSystem.getDevice().createCommandEncoder();
        *///?}

        var r = (configData.balloonTint() >> 16) & 255;
        var g = (configData.balloonTint() >> 8) & 255;
        var b = configData.balloonTint() & 255;

        for (int i = 0; i < messages.size(); i++) {
            //? if >= 1.21.5 {
            /*var builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            *///?} else if >= 1.21.1 {
            /*var builder = bufferSource.getBuffer(RenderType.guiTextured(balloonTexture));
            *///?} else {
            var builder = bufferSource.getBuffer(RenderType.entityTranslucent(balloonTexture));
            //?}

            Component message = messages.get(i);
            poseStack.pushPose();

            poseStack.translate(0.0, playerHeight + TalkBalloons.config.balloonsHeightOffset - 0.4 + (padding / 32.0), 0.0D);
            poseStack.mulPose(rotation);
            poseStack.scale(-0.025F, -0.025F, 0.025F);

            //? if < 1.21.5 {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.enablePolygonOffset();
            RenderSystem.polygonOffset(3.0F, 3.0F);
            //?}
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
            blit(poseStack, builder, -baseX - 3 - padding, baseY - balloonDistance - padding, 5, 5, 0.0F, 0.0F, 5, 5, 32, 32); // TOP
            blit(poseStack, builder, -baseX - 3 - padding, baseY + 5 - balloonDistance - padding, 5, balloonHeight + j * 8 + (padding * 2), 0.0F, 6.0F, 5, 1, 32, 32); // MID
            blit(poseStack, builder, -baseX - 3 - padding, 5 - balloonDistance + padding, 5, 5, 0.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

            // Mid
            blit(poseStack, builder, -baseX + 2 - padding, baseY - balloonDistance - padding, balloonWidth - 4 + (padding * 2), 5, 6.0F, 0.0F, 5, 5, 32, 32); // TOP
            blit(poseStack, builder, -baseX + 2 - padding, baseY + 5 - balloonDistance - padding, balloonWidth - 4 + (padding * 2), balloonHeight + j * 8 + (padding * 2), 6.0F, 6.0F, 5, 1, 32, 32); // MID
            blit(poseStack, builder, -baseX + 2 - padding, 5 - balloonDistance + padding, balloonWidth - 4 + (padding * 2), 5, 6.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

            // Right
            blit(poseStack, builder, baseX - 1 + padding, baseY - balloonDistance - padding, 5, 5, 12.0F, 0.0F, 5, 5, 32, 32); // TOP
            blit(poseStack, builder, baseX - 1 + padding, baseY + 5 - balloonDistance - padding, 5, balloonHeight + j * 8 + (padding * 2), 12.0F, 6.0F, 5, 1, 32, 32); // MID
            blit(poseStack, builder, baseX - 1 + padding, 5 - balloonDistance + padding, 5, 5, 12.0F, 8.0F, 5, 5, 32, 32); // BOTTOM

            //? if < 1.21.5 {
            RenderSystem.polygonOffset(0.0F, 0.0F);
            RenderSystem.disablePolygonOffset();
            //?}

            // Arrow
            if (i == 0)
                blit(poseStack, builder, -3, 9 + padding, 7, 4, 18, 6, 7, 4, 32, 32);

            //? if < 1.21.5 {
            RenderSystem.disableBlend();
            //?} else {
            /*try (MeshData meshData = builder.buildOrThrow()) {
                var vertexBuffer = DefaultVertexFormat.POSITION_TEX_COLOR.uploadImmediateVertexBuffer(meshData.vertexBuffer());
                var indexBufferStorage = RenderSystem.getSequentialBuffer(meshData.drawState().mode());
                var indexBuffer = indexBufferStorage.getBuffer(meshData.drawState().indexCount());
                var indexType = indexBufferStorage.type();

                try (RenderPass pass = encoder.createRenderPass(renderTarget.getColorTexture(), OptionalInt.empty(), renderTarget.getDepthTexture(), OptionalDouble.empty())) {
                    pass.bindSampler("Sampler0", balloonGpuTexture);
                    pass.setVertexBuffer(0, vertexBuffer);
                    pass.setIndexBuffer(indexBuffer, indexType);

                    pass.setPipeline(MAIN_BALLOON_PIPELINE);
                    pass.drawIndexed(0, 6 * 3 * 3);
                    pass.setPipeline(BALLOON_ARROW_PIPELINE);
                    pass.drawIndexed(6 * 3 * 3, 6);
                }
            }
            *///?}
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

    private static void blit(PoseStack poseStack, VertexConsumer consumer, int x, int y, int width, int height, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        var matrix = poseStack.last().pose();

        var x2 = x + width;
        var y2 = y + height;
        var minU = uOffset / textureWidth;
        var maxU = (uOffset + uWidth) / textureWidth;
        var minV = vOffset / textureHeight;
        var maxV = (vOffset + vHeight) / textureHeight;

        //? if >= 1.20.6 {
        /*consumer.addVertex(matrix, (float) x, (float) y, 0f)
            .setUv(minU, minV)
            .setColor(-1);

        consumer.addVertex(matrix, (float) x, (float) y2, 0f)
            .setUv(minU, maxV)
            .setColor(-1);

        consumer.addVertex(matrix, (float) x2, (float) y2, 0f)
            .setUv(maxU, maxV)
            .setColor(-1);

        consumer.addVertex(matrix, (float) x2, (float) y, 0f)
            .setUv(maxU, minV)
            .setColor(-1);
        *///?} else {
        consumer.vertex(matrix, (float) x, (float) y, 0f)
            .uv(minU, minV)
            .color(-1)
            .endVertex();

        consumer.vertex(matrix, (float) x, (float) y2, 0f)
            .uv(minU, maxV)
            .color(-1)
            .endVertex();

        consumer.vertex(matrix, (float) x2, (float) y2, 0f)
            .uv(maxU, maxV)
            .color(-1)
            .endVertex();

        consumer.vertex(matrix, (float) x2, (float) y, 0f)
            .uv(maxU, minV)
            .color(-1)
            .endVertex();
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
