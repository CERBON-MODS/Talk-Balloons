package com.cerbon.talk_balloons.client.resources

import com.cerbon.talk_balloons.TalkBalloons
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureManager
//? if <= 1.21.8 {
import net.minecraft.client.resources.TextureAtlasHolder
//? }
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection
import net.minecraft.client.resources.metadata.gui.GuiMetadataSection
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}

//? if <= 1.21.8 {
class BalloonSpriteManager(textureManager: TextureManager) : TextureAtlasHolder(textureManager, BalloonStyle.BALLOONS_SHEET, BalloonStyle.BALLOONS_ATLAS, setOf(
    //? if < 1.21.4 {
    AnimationMetadataSection.SERIALIZER,
    //? } else {
    /*AnimationMetadataSection.TYPE,
    *///? }
    GuiMetadataSection.TYPE
)) {
    fun getMetadata(sprite: TextureAtlasSprite): GuiMetadataSection {
        return sprite.contents().metadata().getSection(GuiMetadataSection.TYPE)
            .orElse(GuiMetadataSection.DEFAULT) ?: GuiMetadataSection.DEFAULT
    }

    fun getSpriteAccess(id: Identifier): TextureAtlasSprite {
        return super.getSprite(id)
    }

    companion object {
        @JvmField val ID: Identifier = TalkBalloons.id("balloon_sprite_manager")
    }
}
//? }
