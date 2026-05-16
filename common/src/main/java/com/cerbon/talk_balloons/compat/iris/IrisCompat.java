package com.cerbon.talk_balloons.compat.iris;

import net.irisshaders.iris.api.v0.IrisApi;

public class IrisCompat {
    public static boolean isInShadowPass() {
        return IrisApi.getInstance().isRenderingShadowPass();
    }
}
