package com.cerbon.talk_balloons.client

import com.cerbon.talk_balloons.TalkBalloons
import com.cerbon.talk_balloons.client.resources.BalloonSpriteManager
import com.cerbon.talk_balloons.client.resources.BalloonStyle
import com.cerbon.talk_balloons.client.resources.BalloonStyleManager
import com.cerbon.talk_balloons.compat.CompatHandler
import com.cerbon.talk_balloons.compat.iris.IrisCompat
import com.cerbon.talk_balloons.util.HistoricalData
import com.cerbon.talk_balloons.util.SynchronizedConfigData
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferUploader
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling
import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence
import net.minecraft.util.Mth
import kotlin.math.min

object BalloonRenderer {
    @JvmField val SPRITE_MANAGER = BalloonSpriteManager(Minecraft.getInstance().textureManager)

    @JvmStatic
    fun renderBalloons(poseStack: PoseStack, cameraYaw: Float, font: Font, messages: HistoricalData<Component>, playerHeight: Float, configData: SynchronizedConfigData, light: Int) {
        if (messages.isEmpty())
            return

        val style = BalloonStyleManager.getStyleById(configData.balloonStyle)
        val balloonSprite = SPRITE_MANAGER.getSpriteAccess(style.balloon)
        val arrowSprite = SPRITE_MANAGER.getSpriteAccess(style.arrow)

        val consumer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE)

        val padding = configData.balloonPadding
        val textColor = configData.textColor or (0xFF shl 24)
        val balloonTint = if (style.allowsTint)
            configData.balloonTint or (0xFF shl 24)
        else -1
        val fontHeight = font.lineHeight

        var balloonDistance = 0f

        messages.asReversed().forEachIndexed { index, message ->
            poseStack.pushPose()

            poseStack.translate(0.0f, playerHeight + TalkBalloons.config.balloonsHeightOffset, 0.0f)
            poseStack.mulPose(Axis.YP.rotationDegrees(-cameraYaw))
            poseStack.scale(-0.025f, -0.025f, -0.025f)

            val dividedMessage = font.split(message, TalkBalloons.config.maxBalloonWidth)
            val greatestTextWidth = dividedMessage.maxOf { font.width(it) }

            var textDistance = 0

            val balloonWidth = Mth.clamp(greatestTextWidth, TalkBalloons.config.minBalloonWidth, TalkBalloons.config.maxBalloonWidth)
            val actualBalloonWidth = balloonWidth + (padding * 2) + style.margins.horizontalMargins
            val baseX = -(actualBalloonWidth / 2f)

            if (dividedMessage.size > 1) {
                for (text in dividedMessage) {
                    drawString(poseStack.last(), font, text, -font.width(text) / 2f + 0.5f, -(fontHeight * dividedMessage.size) - balloonDistance + textDistance, textColor, false, light)
                    textDistance += fontHeight
                }
            } else {
                drawString(poseStack.last(), font, message.visualOrderText, -greatestTextWidth / 2f + 0.5f, -(fontHeight * dividedMessage.size) - balloonDistance, textColor, false, light)
                textDistance += fontHeight
            }

            val balloonHeight = textDistance + (padding * 2) + style.margins.verticalMargins - 2
            blitSprite(poseStack.last(), consumer, balloonSprite, baseX, -balloonDistance - balloonHeight, actualBalloonWidth, balloonHeight, balloonTint, light = light)

            if (index == 0) {
                blitSprite(poseStack.last(), consumer, arrowSprite, -(arrowSprite.contents().width() / 2f), -1f, arrowSprite.contents().width(), arrowSprite.contents().height(), balloonTint, 0.001f, light)
            }

            balloonDistance += balloonHeight + TalkBalloons.config.distanceBetweenBalloons

            poseStack.popPose()
        }

        val meshData = consumer.build()
        if (meshData != null) {
            RenderSystem.enableBlend()
            RenderSystem.defaultBlendFunc()
            RenderSystem.enableDepthTest()
            RenderSystem.enablePolygonOffset()
            RenderSystem.polygonOffset(3f, 3f)

            RenderSystem.setShader(GameRenderer::getParticleShader)
            RenderSystem.setShaderTexture(0, BalloonStyle.BALLOONS_SHEET)
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer()
            BufferUploader.drawWithShader(meshData)

            RenderSystem.disableDepthTest()
            RenderSystem.disableBlend()
            RenderSystem.disablePolygonOffset()
        }
    }

    private fun drawString(pose: PoseStack.Pose, font: Font, text: FormattedCharSequence, x: Float, y: Float, color: Int, dropShadow: Boolean, light: Int) {
        // otherwise text looks wonk af
        if (CompatHandler.isIrisLoaded && IrisCompat.isInShadowPass())
            return

        font.drawInBatch(text, x, y, color, dropShadow, pose.pose(), Minecraft.getInstance().renderBuffers().bufferSource(), Font.DisplayMode.NORMAL, 0, light)
    }

    private fun blitSprite(pose: PoseStack.Pose, consumer: VertexConsumer, sprite: TextureAtlasSprite, x: Float, y: Float, width: Int, height: Int, color: Int = -1, z: Float = 0f, light: Int) {
        val scaling = SPRITE_MANAGER.getMetadata(sprite).scaling

        if (scaling is GuiSpriteScaling.Stretch) {
            this.blitDirect(pose, consumer, x, y, width.toFloat(), height.toFloat(), sprite.u0, sprite.v0, sprite.u1, sprite.v1, color, z, light)
        } else if (scaling is GuiSpriteScaling.Tile) {
            this.blitTiled(pose, consumer, sprite, x, y, width, height, 0f, 0f, scaling.width, scaling.height, scaling.width, scaling.height, color, z, light)
        } else if (scaling is GuiSpriteScaling.NineSlice) {
            val border = scaling.border
            val leftBorder = border.left.coerceAtMost(width / 2)
            val rightBorder = border.right.coerceAtMost(width / 2)
            val topBorder = border.top.coerceAtMost(height / 2)
            val bottomBorder = border.bottom.coerceAtMost(height / 2)

            if (width == scaling.width && height == scaling.height) {
                this.blitFromSprite(pose, consumer, sprite, x, y, scaling.width, scaling.height, 0f, 0f, width.toFloat(), height.toFloat(), color, z, light)
            } else if (height == scaling.height()) {
                this.blitFromSprite(pose, consumer, sprite, x, y, scaling.width(), scaling.height(), 0f, 0.toFloat(), leftBorder.toFloat(), height.toFloat(), color, z, light)
                this.blitTiled(pose, consumer, sprite, x + leftBorder, y, width - rightBorder - leftBorder, height, leftBorder.toFloat(), 0.toFloat(), scaling.width() - rightBorder - leftBorder, scaling.height(), scaling.width(), scaling.height(), color, z, light)
                this.blitFromSprite(pose, consumer, sprite, x + width - rightBorder, y, scaling.width(), scaling.height(), scaling.width() - rightBorder.toFloat(), 0.toFloat(), rightBorder.toFloat(), height.toFloat(), color, z, light)
            } else if (width == scaling.width()) {
                this.blitFromSprite(pose, consumer, sprite, x, y, scaling.width(), scaling.height(), 0.toFloat(), 0.toFloat(), width.toFloat(), topBorder.toFloat(), color, z, light)
                this.blitTiled(pose, consumer, sprite, x, y + topBorder, width, height - bottomBorder - topBorder, 0.toFloat(), topBorder.toFloat(), scaling.width(), scaling.height() - bottomBorder - topBorder, scaling.width(), scaling.height(), color, z, light)
                this.blitFromSprite(pose, consumer, sprite, x, y + height - bottomBorder, scaling.width(), scaling.height(), 0.toFloat(), scaling.height() - bottomBorder.toFloat(), width.toFloat(), bottomBorder.toFloat(), color, z, light)
            } else {
                this.blitFromSprite(pose, consumer, sprite, x, y, scaling.width(), scaling.height(), 0.toFloat(), 0.toFloat(), leftBorder.toFloat(), topBorder.toFloat(), color, z, light)
                this.blitTiled(pose, consumer, sprite, x + leftBorder, y, width - rightBorder - leftBorder, topBorder, leftBorder.toFloat(), 0f, scaling.width() - rightBorder - leftBorder, topBorder, scaling.width(), scaling.height(), color, z, light)
                this.blitFromSprite(pose, consumer, sprite, x + width - rightBorder, y, scaling.width(), scaling.height(), scaling.width() - rightBorder.toFloat(), 0f, rightBorder.toFloat(), topBorder.toFloat(), color, z, light)

                this.blitFromSprite(pose, consumer, sprite, x, y + height - bottomBorder, scaling.width(), scaling.height(), 0f, scaling.height() - bottomBorder.toFloat(), leftBorder.toFloat(), bottomBorder.toFloat(), color, z, light)
                this.blitTiled(pose, consumer, sprite, x + leftBorder, y + height - bottomBorder, width - rightBorder - leftBorder, bottomBorder, leftBorder.toFloat(), scaling.height() - bottomBorder.toFloat(), scaling.width() - rightBorder - leftBorder, bottomBorder, scaling.width(), scaling.height(), color, z, light)
                this.blitFromSprite(pose, consumer, sprite, x + width - rightBorder, y + height - bottomBorder, scaling.width(), scaling.height(), scaling.width() - rightBorder.toFloat(), scaling.height() - bottomBorder.toFloat(), rightBorder.toFloat(), bottomBorder.toFloat(), color, z, light)

                this.blitTiled(pose, consumer, sprite, x, y + topBorder, leftBorder, height - bottomBorder - topBorder, 0f, topBorder.toFloat(), leftBorder, scaling.height() - bottomBorder - topBorder, scaling.width(), scaling.height(), color, z, light)
                this.blitTiled(pose, consumer, sprite, x + leftBorder, y + topBorder, width - rightBorder - leftBorder, height - bottomBorder - topBorder, leftBorder.toFloat(), topBorder.toFloat(), scaling.width() - rightBorder - leftBorder, scaling.height() - bottomBorder - topBorder, scaling.width(), scaling.height(), color, z, light)
                this.blitTiled(pose, consumer, sprite, x + width - rightBorder, y + topBorder, leftBorder, height - bottomBorder - topBorder, scaling.width() - rightBorder.toFloat(), topBorder.toFloat(), rightBorder, scaling.height() - bottomBorder - topBorder, scaling.width(), scaling.height(), color, z, light)
            }
        }
    }

    private fun blitTiled(pose: PoseStack.Pose, consumer: VertexConsumer, sprite: TextureAtlasSprite, x: Float, y: Float, width: Int, height: Int, uOffset: Float, vOffset: Float, uWidth: Int, vHeight: Int, nineSliceWidth: Int, nineSliceHeight: Int, color: Int = -1, z: Float = 0f, light: Int) {
        if (width > 0 && height > 0) {
            if (uWidth <= 0 || vHeight <= 0)
                throw IllegalArgumentException("Tiled sprite texture size must be positive, got ${uWidth}x${vHeight}")

            var i = 0
            while (i < width) {
                val j = min(uWidth, width - i)

                var k = 0
                while (k < height) {
                    val l = min(vHeight, height - k)
                    this.blitFromSprite(pose, consumer, sprite, x + i, y + k, nineSliceWidth, nineSliceHeight, uOffset, vOffset, j.toFloat(), l.toFloat(), color, z, light)
                    k += vHeight
                }

                i += uWidth
            }
        }
    }

    private fun blitFromSprite(pose: PoseStack.Pose, consumer: VertexConsumer, sprite: TextureAtlasSprite, x: Float, y: Float, texWidth: Int, texHeight: Int, uOffset: Float, vOffset: Float, uWidth: Float, vHeight: Float, color: Int = -1, z: Float = 0f, light: Int) {
        this.blitDirect(pose, consumer, x, y, uWidth, vHeight,
            sprite.getU(uOffset / texWidth), sprite.getV(vOffset / texHeight),
            sprite.getU((uOffset + uWidth) / texWidth), sprite.getV((vOffset + vHeight) / texHeight),
            color, z, light
        )
    }

    private fun blit(pose: PoseStack.Pose, consumer: VertexConsumer, x: Float, y: Float, width: Int, height: Int, uOffset: Float, vOffset: Float, uWidth: Float, vHeight: Float, color: Int = -1, z: Float = 0f, light: Int) {
        this.blitDirect(pose, consumer, x, y, width.toFloat(), height.toFloat(), uOffset, vOffset, uOffset + uWidth, vOffset + vHeight, color, z, light)
    }

    private fun blitDirect(pose: PoseStack.Pose, consumer: VertexConsumer, x: Float, y: Float, width: Float, height: Float, u0: Float, v0: Float, u1: Float, v1: Float, color: Int = -1, z: Float = 0f, light: Int) {
        val x2 = (x + width)
        val y2 = (y + height)

        consumer.addVertex(pose, x, y, z)
            .setUv(u0, v0)
            .setColor(color)
            .setLight(light)

        consumer.addVertex(pose, x, y2, z)
            .setUv(u0, v1)
            .setColor(color)
            .setLight(light)

        consumer.addVertex(pose, x2, y2, z)
            .setUv(u1, v1)
            .setColor(color)
            .setLight(light)

        consumer.addVertex(pose, x2, y, z)
            .setUv(u1, v0)
            .setColor(color)
            .setLight(light)
    }
}
