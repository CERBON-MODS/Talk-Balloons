package com.cerbon.talk_balloons.mixin.accessor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {

    @Invoker("<init>")
    static GuiGraphics getGuiGraphics(Minecraft client, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource) {
        throw new AssertionError();
    }
}
