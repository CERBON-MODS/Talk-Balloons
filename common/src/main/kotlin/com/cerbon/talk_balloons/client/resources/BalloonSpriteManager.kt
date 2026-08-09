package com.cerbon.talk_balloons.client.resources

import com.cerbon.talk_balloons.TalkBalloons
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureManager
//? if <= 1.21.8 {
import net.minecraft.client.resources.TextureAtlasHolder
//? }

//? if > 1.20.1 {
/*import net.minecraft.client.resources.metadata.gui.GuiMetadataSection
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection
*///? }
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}

//? if <= 1.20.1 {
import net.minecraft.server.packs.metadata.MetadataSectionType
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.cerbon.talk_balloons.util.mixin.IGuiSectionProvider
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import net.minecraft.server.packs.metadata.MetadataSectionSerializer
//? }

//? if <= 1.21.8 {
class BalloonSpriteManager(textureManager: TextureManager) : TextureAtlasHolder(textureManager, BalloonStyle.BALLOONS_SHEET, BalloonStyle.BALLOONS_ATLAS,
    //? if > 1.20.1 {
    /*setOf(
        //? if < 1.21.4 {
        AnimationMetadataSection.SERIALIZER,
        //? } else {
        /^AnimationMetadataSection.TYPE,
        ^///? }
        GuiMetadataSection.TYPE
    )
    *///? }
) {
    fun getMetadata(sprite: TextureAtlasSprite): GuiMetadataSection {
        //? if <= 1.20.1 {
        return (sprite.contents() as IGuiSectionProvider).`talk_balloons$getSection`()
        //? } else {
        /*return sprite.contents().metadata().getSection(GuiMetadataSection.TYPE)
            .orElse(GuiMetadataSection.DEFAULT) ?: GuiMetadataSection.DEFAULT
        *///? }
    }

    fun getSpriteAccess(id: Identifier): TextureAtlasSprite {
        return super.getSprite(id)
    }

    companion object {
        @JvmField val ID: Identifier = TalkBalloons.id("balloon_sprite_manager")

        //? if <= 1.20.1 {
        @JvmField val SECTION_TYPE: MetadataSectionType<GuiMetadataSection> = MetadataSectionType.fromCodec("gui", GuiMetadataSection.CODEC)
        @JvmField val SECTION_SERIALIZER: MetadataSectionSerializer<GuiMetadataSection> = object : MetadataSectionSerializer<GuiMetadataSection> {
            override fun getMetadataSectionName(): String = "gui"

            override fun fromJson(obj: JsonObject): GuiMetadataSection {
                val scalingObj = obj.getAsJsonObject("scaling")
                val typeStr = scalingObj.get("type").asString
                val type = GuiSpriteScaling.Type.valueOf(typeStr.uppercase())

                val scaling = type.codec.decode(JsonOps.INSTANCE, scalingObj).get().orThrow().first
                return GuiMetadataSection(scaling)
            }
        }
        //? }
    }

    //? if <= 1.20.1 {
    @JvmRecord
    data class GuiMetadataSection(val scaling: GuiSpriteScaling) {
        companion object {
            @JvmField val EMPTY = GuiMetadataSection(GuiSpriteScaling.Stretch)

            @JvmField val CODEC: Codec<GuiMetadataSection> = RecordCodecBuilder.create { instance ->
                instance.group(
                    GuiSpriteScaling.CODEC.fieldOf("scaling")
                        .forGetter(GuiMetadataSection::scaling)
                )
                    .apply(instance, ::GuiMetadataSection)
            }
        }
    }
    //? }
}
//? }
