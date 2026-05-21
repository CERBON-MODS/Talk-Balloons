package com.cerbon.talk_balloons.compat.iris;

//? if >= 1.21.5 {
/*import com.cerbon.talk_balloons.client.BalloonRenderer;
import net.irisshaders.iris.api.v0.IrisProgram;
*///? }
import net.irisshaders.iris.api.v0.IrisApi;

public class IrisCompat {
    public static boolean isInShadowPass() {
        return IrisApi.getInstance().isRenderingShadowPass();
    }

    public static void setupPipelines() {
        //? if >= 1.21.5 {
        /*IrisApi.getInstance().assignPipeline(BalloonRenderer.BALLOON_PIPELINE, IrisProgram.PARTICLES_TRANSLUCENT);
        *///? }
    }
}
