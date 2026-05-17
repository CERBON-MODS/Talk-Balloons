package com.cerbon.talk_balloons.mixin;

//? if >= 1.21.10 {
/*import com.cerbon.talk_balloons.util.mixin.ICameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.renderer.state.CameraRenderState;

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
