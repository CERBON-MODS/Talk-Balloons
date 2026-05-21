package com.cerbon.talk_balloons.client

import com.cerbon.talk_balloons.TalkBalloons
//? if < 1.21.9 {
import com.cerbon.talk_balloons.client.resources.BalloonSpriteManager
//? } else {
/*import net.minecraft.client.resources.metadata.gui.GuiMetadataSection
*///? }
import com.cerbon.talk_balloons.client.resources.BalloonStyle
import com.cerbon.talk_balloons.client.resources.BalloonStyleManager
import com.cerbon.talk_balloons.compat.CompatHandler
import com.cerbon.talk_balloons.compat.iris.IrisCompat
import com.cerbon.talk_balloons.config.ITBConfig
import com.cerbon.talk_balloons.util.HistoricalData
import com.cerbon.talk_balloons.config.SynchronizedConfigData
import com.cerbon.talk_balloons.util.BalloonData
//? if >= 1.21.5 {
/*import com.mojang.blaze3d.buffers.GpuBuffer
//? if <= 1.21.5 {
import com.mojang.blaze3d.buffers.BufferType
import com.mojang.blaze3d.buffers.BufferUsage
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.textures.GpuTexture
import net.minecraft.client.renderer.texture.AbstractTexture
//? }
import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
//? if <= 1.21.11 {
import com.mojang.blaze3d.platform.DepthTestFunction
//? } else {
/*import com.mojang.blaze3d.pipeline.ColorTargetState
import com.mojang.blaze3d.pipeline.DepthStencilState
import com.mojang.blaze3d.platform.CompareOp
*///? }
import com.mojang.blaze3d.shaders.UniformType
import java.util.OptionalDouble
import java.util.OptionalInt
*///? }

//? if >= 1.21.11 {
/*import com.mojang.blaze3d.textures.FilterMode
*///? }

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.ByteBufferBuilder
//? if <= 1.21.4 {
import com.mojang.blaze3d.vertex.BufferUploader
//? }
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
//? if < 1.21.3 {
import net.minecraft.client.renderer.GameRenderer
 //? } else if <= 1.21.4 {
/*import net.minecraft.client.renderer.CoreShaders
*///? }
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling
import net.minecraft.util.FormattedCharSequence
import net.minecraft.util.Mth
import java.util.AbstractQueue
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.min

//? if >= 1.21.8 {
/*import com.mojang.blaze3d.textures.GpuTextureView

//? if > 1.21.8 {
/*import net.minecraft.client.gui.font.TextRenderable
*///? } else {
import net.minecraft.client.gui.font.glyphs.BakedGlyph
//? }

import net.minecraft.client.renderer.RenderPipelines
*///? }

//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//? } else {
/*import com.mojang.blaze3d.textures.GpuSampler
*///? }

//? if >= 1.21.4 {
/*import com.mojang.blaze3d.vertex.MeshData
*///? }

object BalloonRenderer {
    //? if < 1.21.9 {
    @JvmField val SPRITE_MANAGER = BalloonSpriteManager(Minecraft.getInstance().textureManager)
    //? }

    //? if >= 1.21.6 {
    /*private object BufferType {
        const val VERTICES = GpuBuffer.USAGE_VERTEX
        const val INDICES = GpuBuffer.USAGE_INDEX
    }
    *///? } else if >= 1.21.5 {
    
    /*private val RenderTarget.colorTextureView: GpuTexture?
        get() = this.colorTexture

    private val RenderTarget.depthTextureView: GpuTexture?
        get() = this.depthTexture

    private val AbstractTexture.textureView: GpuTexture
        get() = this.texture
    *///? }

    //? if >= 1.21.5 {
    /*@JvmField val BALLOON_PIPELINE: RenderPipeline = RenderPipeline.builder()
        //? if <= 1.21.5 {
        // Matrices snippet
        .withUniform("ModelViewMat", UniformType.MATRIX4X4)
        .withUniform("ProjMat", UniformType.MATRIX4X4)
        // Color snippet
        .withUniform("ColorModulator", UniformType.VEC4)
        // Fog (no-color) snippet
        .withUniform("FogStart", UniformType.FLOAT)
        .withUniform("FogEnd", UniformType.FLOAT)
        .withUniform("FogShape", UniformType.INT)
        // Fog (color) snippet
        .withUniform("FogColor", UniformType.VEC4)
        //? } else {
        /*// Globals snippet
        .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
        .withUniform("Projection", UniformType.UNIFORM_BUFFER)
        .withUniform("Fog", UniformType.UNIFORM_BUFFER)
        *///? }
        // Particle snippet
        .withVertexShader("core/particle")
        .withFragmentShader("core/particle")
        .withSampler("Sampler0")
        .withSampler("Sampler2")
        .withVertexFormat(DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS)
        // Balloon (ours)
        .withLocation(TalkBalloons.id("balloon"))
        //? if <= 1.21.11 {
        .withBlend(BlendFunction.TRANSLUCENT)
        .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST) // enable depth test
        .withDepthBias(3f, 3f) // polygon offset
        //? } else {
        /*.withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
        .withDepthStencilState(DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true, 3f, 3f))
        *///? }
        .build()
    *///? }

    private val bufferBuilder = ByteBufferBuilder(1 * 1024 * 1024) // 1 MiB of data max
    //? if >= 1.21.8 {
    /*private val textBufferBuilder = ByteBufferBuilder(4 * 1024 * 1024) // 4 MiB of data max
    *///? }

    private val renderQueue = ConcurrentLinkedQueue<QueuedBalloonRender>()

    //? if >= 1.21.5 {
    /*init {
        if (CompatHandler.isIrisLoaded) {
            IrisCompat.setupPipelines()
        }
    }
    *///? }

    @JvmStatic
    fun calculateEstimatedBalloonHeight(messages: List<BalloonData>, font: Font, configData: SynchronizedConfigData, config: ITBConfig): Int {
        val style = BalloonStyleManager.getStyleById(configData.balloonStyle.orElse(config.balloonStyle.identifier)!!)
        val padding = configData.balloonPadding.orElse(config.balloonPadding)!!
        val fontHeight = font.lineHeight
        val dividedMessages = messages.map { font.split(it.text, config.maxBalloonWidth) }

        return dividedMessages.sumOf { ((padding * 2) + style.margins.verticalMargins + it.size * fontHeight) + config.distanceBetweenBalloons }
    }

    @JvmStatic @JvmOverloads
    fun submitBalloons(poseStack: PoseStack, cameraYaw: Float, font: Font, messages: HistoricalData<BalloonData>, playerHeight: Float, isSneaking: Boolean, configData: SynchronizedConfigData, light: Int, renderQueue: AbstractQueue<QueuedBalloonRender> = this.renderQueue, config: ITBConfig = TalkBalloons.config) {
        if (messages.isEmpty())
            return

        if (CompatHandler.isIrisLoaded && IrisCompat.isInShadowPass())
            return

        val style = BalloonStyleManager.getStyleById(configData.balloonStyle.orElse(config.balloonStyle.identifier)!!)
        //? if < 1.21.9 {
        val balloonSprite = SPRITE_MANAGER.getSpriteAccess(style.balloon)
        val arrowSprite = SPRITE_MANAGER.getSpriteAccess(style.arrow)
        //? } else {
        /*val balloonAtlas = Minecraft.getInstance().atlasManager.getAtlasOrThrow(BalloonStyle.BALLOONS_ATLAS)
        val balloonSprite = balloonAtlas.getSprite(style.balloon)
        val arrowSprite = balloonAtlas.getSprite(style.arrow)
        *///? }

        val consumer = BufferBuilder(this.bufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE)
        //? if >= 1.21.8 {
        /*val textConsumers = mutableMapOf<GpuTextureView, BufferBuilder>()
        *///? }

        val defaultBalloonOpacity = if (isSneaking) config.balloonSneakingOpacity else config.balloonOpacity
        val padding = configData.balloonPadding.orElse(config.balloonPadding)!!
        val defaultTextColor = configData.textColor.orElse(config.textColor)!! and 0x00FFFFFF
        val defaultBalloonTint = (if (style.allowsTint)
            configData.balloonTint.orElse(config.balloonTint)!!
        else 0xFFFFFF) and 0x00FFFFFF
        val fontHeight = font.lineHeight

        var balloonDistance = 0f

        val currentTime = System.currentTimeMillis()
        val fadeOutMs = (config.balloonFadeOut * 1000).toInt()

        messages.asReversed().forEachIndexed { index, message ->
            val fadeAmount = if (message.ticksToLive > 0 && currentTime - message.creationTime >= (message.ticksToLive * 50) - fadeOutMs)
                Mth.clamp(((message.ticksToLive * 50) - (currentTime - message.creationTime)) / fadeOutMs.toFloat(), 0f, 1f)
            else 1f
            val balloonOpacity = ((defaultBalloonOpacity * fadeAmount) * 255).toInt()
                .coerceAtLeast(10) // funny bug, when we're at 0 the text becomes full opacity
            val textColor = defaultTextColor or (balloonOpacity shl 24)
            val balloonTint = defaultBalloonTint or (balloonOpacity shl 24)

            poseStack.pushPose()

            poseStack.translate(0.0f, playerHeight + config.balloonsHeightOffset, 0.0f)
            poseStack.mulPose(Axis.YP.rotationDegrees(-cameraYaw))
            poseStack.scale(-0.025f, -0.025f, -0.025f)

            val dividedMessage = font.split(message.text, config.maxBalloonWidth)
            val greatestTextWidth = dividedMessage.maxOf { font.width(it) }

            var textDistance = 0

            val balloonWidth = Mth.clamp(greatestTextWidth, config.minBalloonWidth, config.maxBalloonWidth)
            val actualBalloonWidth = balloonWidth + (padding * 2) + style.margins.horizontalMargins
            val baseX = -(actualBalloonWidth / 2f)
            val baseY = -((padding - 1f).coerceAtLeast(0f))

            if (dividedMessage.size > 1) {
                for (text in dividedMessage) {
                    drawString(poseStack.last(), font, text, -font.width(text) / 2f + 0.5f, baseY - (fontHeight * dividedMessage.size) - balloonDistance + textDistance, textColor, false, light,
                        //? if >= 1.21.8 {
                        /*textConsumers
                        *///? }
                    )
                    textDistance += fontHeight
                }
            } else {
                drawString(poseStack.last(), font, message.text.visualOrderText, -greatestTextWidth / 2f + 0.5f, baseY - (fontHeight * dividedMessage.size) - balloonDistance, textColor, false, light,
                    //? if >= 1.21.8 {
                    /*textConsumers
                    *///? }
                )
                textDistance += fontHeight
            }

            val balloonHeight = textDistance + (padding * 2) + style.margins.verticalMargins - 2
            blitSprite(poseStack.last(), consumer, balloonSprite, baseX, -balloonDistance - balloonHeight, actualBalloonWidth, balloonHeight, balloonTint, light = light)

            if (index == 0) {
                blitSprite(poseStack.last(), consumer, arrowSprite, -(arrowSprite.contents().width() / 2f), -1f, arrowSprite.contents().width(), arrowSprite.contents().height(), balloonTint, 0.01f, light)
            }

            balloonDistance += balloonHeight + config.distanceBetweenBalloons

            poseStack.popPose()
        }

        val meshData = consumer.build()
        if (meshData != null) {
            renderQueue.add(QueuedBalloonRender(meshData,
                //? if >= 1.21.8 {
                /*textConsumers
                    .mapValues { it.value.build() }
                    .filter { it.value != null }
                    .mapValues { it.value!! },
                *///? }
            ))
        }
    }

    @JvmStatic @JvmOverloads
    fun renderBalloons(queue: AbstractQueue<QueuedBalloonRender> = this.renderQueue) {
        if (CompatHandler.isIrisLoaded && IrisCompat.isInShadowPass())
            return

        while (queue.isNotEmpty()) {
            val (meshData, textBuffers) = queue.remove()

            //? if <= 1.21.4 {
            RenderSystem.enableBlend()
            RenderSystem.defaultBlendFunc()
            RenderSystem.enableDepthTest()
            RenderSystem.enablePolygonOffset()
            RenderSystem.polygonOffset(3f, 3f)

            //? if < 1.21.3 {
            RenderSystem.setShader(GameRenderer::getParticleShader)
            //? } else if <= 1.21.4 {
            /*RenderSystem.setShader(CoreShaders.PARTICLE)
            *///? }
            RenderSystem.setShaderTexture(0, BalloonStyle.BALLOONS_SHEET)
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer()
            BufferUploader.drawWithShader(meshData)

            RenderSystem.disableDepthTest()
            RenderSystem.disableBlend()
            RenderSystem.disablePolygonOffset()
            //? } else {
            /*val texture = Minecraft.getInstance().textureManager.getTexture(BalloonStyle.BALLOONS_SHEET)
            drawBuffer("Balloon", this.bufferBuilder, meshData,
                //? if >= 1.21.8 {
                /*texture.textureView,

                //? if >= 1.21.11 {
                /*texture.sampler,
                *///? }

                *///? } else {
                BalloonStyle.BALLOONS_SHEET,
                //? }
                BALLOON_PIPELINE
            )

            //? if >= 1.21.8 {
            /*for ((textureView, buffer) in textBuffers) {
                buffer.sortQuads(this.textBufferBuilder, RenderSystem.getProjectionType().vertexSorting())
                //? if < 26.2 {
                drawBuffer("Text", this.textBufferBuilder, buffer, textureView,
                    //? if >= 1.21.11 {
                    /*RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST),
                    *///? }

                    //? if > 1.21.8 {
                    /*if (queue !== this.renderQueue)
                        RenderPipelines.GUI_TEXT
                    else
                    *///? }
                        RenderPipelines.TEXT
                )
                //? }
            }
            *///? }

            *///? }
        }
    }

    //? if > 1.21.4 {
    /*private fun drawBuffer(
        name: String,
        bufferBuilder: ByteBufferBuilder, meshData: MeshData,

        //? if >= 1.21.8 {
        /*textureView: GpuTextureView,

        //? if >= 1.21.11
        //sampler: GpuSampler,

        *///? } else {
        texture: Identifier,
        //? }
        pipeline: RenderPipeline
    ) {
        val renderTarget = Minecraft.getInstance().mainRenderTarget
        val encoder = RenderSystem.getDevice().createCommandEncoder()

        meshData.sortQuads(bufferBuilder, RenderSystem.getProjectionType().vertexSorting())
        RenderSystem.getDevice().createBuffer({ "Talk Balloons $name Vertex Buffer" }, BufferType.VERTICES,
            //? if <= 1.21.5 {
            BufferUsage.DYNAMIC_WRITE,
            //? }
            meshData.vertexBuffer()
        ).use { vertexBuffer ->
            val indexBuffer = meshData.indexBuffer()?.let { RenderSystem.getDevice().createBuffer({ "Talk Balloons $name Index Buffer" }, BufferType.INDICES,
                //? if <= 1.21.5 {
                BufferUsage.DYNAMIC_WRITE,
                //? }
                it
            ) }

            encoder.createRenderPass(
                //? if >= 1.21.6 {
                /*{ "TalkBalloons $name Render Pass" },
                *///? }
                renderTarget.colorTextureView!!, OptionalInt.empty(), renderTarget.depthTextureView!!, OptionalDouble.empty()
            ).use { pass ->
                //? if >= 1.21.8 {
                /*RenderSystem.bindDefaultUniforms(pass)
                *///? }

                //? if <= 1.21.10 {

                //? if <= 1.21.5 {
                val textureView = Minecraft.getInstance().textureManager.getTexture(texture).textureView
                //? }

                pass.bindSampler("Sampler0", textureView)
                //? if <= 1.21.5 {
                Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer()
                pass.bindSampler("Sampler2", RenderSystem.getShaderTexture(2)!!)
                //? } else {
                /*pass.bindSampler("Sampler2", Minecraft.getInstance().gameRenderer.lightTexture().textureView)
                *///? }
                //? } else {
                /*pass.bindTexture("Sampler0", textureView, sampler)

                //? if <= 1.21.11 {
                val lightTexture = Minecraft.getInstance().gameRenderer.lightTexture()
                pass.bindTexture("Sampler2", lightTexture.textureView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR))
                //? } else {
                /*pass.bindTexture("Sampler2", Minecraft.getInstance().gameRenderer.lightmap(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR))
                *///? }

                *///? }

                pass.setPipeline(pipeline)
                pass.setVertexBuffer(0, vertexBuffer)
                indexBuffer?.let {
                    pass.setIndexBuffer(it, meshData.drawState().indexType)
                }

                //? if <= 1.21.5 {
                pass.drawIndexed(0, meshData.drawState().indexCount)
                //? } else {
                /*pass.drawIndexed(0, 0, meshData.drawState().indexCount, 1)
                *///? }
            }

            indexBuffer?.close()
        }

        meshData.close()
    }
    *///? }

    private fun drawString(
        pose: PoseStack.Pose, font: Font, text: FormattedCharSequence, x: Float, y: Float, color: Int, dropShadow: Boolean, light: Int,
        //? if >= 1.21.8 {
        /*consumers: MutableMap<GpuTextureView, BufferBuilder>
        *///? }
    ) {
        //? if < 1.21.8 {
        // otherwise text looks wonk af
        if (CompatHandler.isIrisLoaded && IrisCompat.isInShadowPass())
            return

        font.drawInBatch(text, x, y, color, dropShadow, pose.pose(), Minecraft.getInstance().renderBuffers().bufferSource(), Font.DisplayMode.NORMAL, 0, light)
        //? } else {
        /*val prepared = font.prepareText(text, x, y, color, dropShadow,
            //? if >= 1.21.11 {
            /*true,
            *///? }
            0,
        )
        prepared.visit(object : Font.GlyphVisitor {
            //? if > 1.21.8 {
            /*override fun acceptEffect(effect: TextRenderable) {
                this.accept(effect)
            }

            //? if >= 1.21.11 {
            /*override fun acceptGlyph(glyph: TextRenderable.Styled) {
                this.accept(glyph)
            }
            *///? } else {
            override fun acceptGlyph(glyph: TextRenderable) {
                this.accept(glyph)
            }
            //? }

            private fun accept(glyph: TextRenderable) {
                val renderType = glyph.renderType(Font.DisplayMode.POLYGON_OFFSET)
                val consumer = consumers.computeIfAbsent(glyph.textureView()!!) { BufferBuilder(this@BalloonRenderer.textBufferBuilder, renderType.mode(), renderType.format()) }
                glyph.render(pose.pose(), consumer, light, false)
            }
            *///? } else {
            override fun acceptEffect(glyph: BakedGlyph, effect: BakedGlyph.Effect) {
                val renderType = glyph.renderType(Font.DisplayMode.POLYGON_OFFSET)
                val consumer = consumers.computeIfAbsent(glyph.textureView()!!) { BufferBuilder(this@BalloonRenderer.textBufferBuilder, renderType.mode(), renderType.format()) }
                glyph.renderEffect(effect, pose.pose(), consumer, light, false)
            }

            override fun acceptGlyph(instance: BakedGlyph.GlyphInstance) {
                val glyph = instance.glyph
                val renderType = glyph.renderType(Font.DisplayMode.POLYGON_OFFSET)
                val consumer = consumers.computeIfAbsent(glyph.textureView()!!) { BufferBuilder(this@BalloonRenderer.textBufferBuilder, renderType.mode(), renderType.format()) }
                glyph.renderChar(instance, pose.pose(), consumer, light, false)
            }
            //? }
        })
        *///? }
    }

    private fun blitSprite(pose: PoseStack.Pose, consumer: VertexConsumer, sprite: TextureAtlasSprite, x: Float, y: Float, width: Int, height: Int, color: Int = -1, z: Float = 0f, light: Int) {
        //? if < 1.21.9 {
        val scaling = SPRITE_MANAGER.getMetadata(sprite).scaling
        //? } else {
        /*val scaling = sprite.contents().getAdditionalMetadata(GuiMetadataSection.TYPE).orElse(GuiMetadataSection.DEFAULT)!!.scaling
        *///? }

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
