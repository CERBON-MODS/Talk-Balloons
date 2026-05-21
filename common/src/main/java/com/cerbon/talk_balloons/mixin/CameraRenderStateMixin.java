package com.cerbon.talk_balloons.mixin;

import com.cerbon.talk_balloons.util.mixin.ICameraRenderState;
//? if >= 1.21.10 {
/*import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if <= 1.21.11 {
import net.minecraft.client.renderer.state.CameraRenderState;
//? } else {
/^import net.minecraft.client.renderer.state.level.CameraRenderState;
^///? }

@Mixin(CameraRenderState.class)
*///? }
public abstract class CameraRenderStateMixin implements ICameraRenderState {
    //? if >= 1.21.10 {
    /*@Unique private float talk_balloons$yaw;

    @Override
    public float talk_balloons$yaw() {
        return this.talk_balloons$yaw;
    }

    @Override
    public void talk_balloons$setYaw(float yaw) {
        this.talk_balloons$yaw = yaw;
    }
    *///? }
}
