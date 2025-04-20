package com.cerbon.talk_balloons.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
//? if >= 1.20
/*import net.minecraft.client.gui.GuiGraphics;*/
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

//? if >= 1.20
/*@Mixin(GuiGraphics.class)*/
public interface GuiGraphicsAccessor {
    //? if >= 1.20
    /*@Accessor @Mutable*/
    void setPose(PoseStack poseStack);
}
