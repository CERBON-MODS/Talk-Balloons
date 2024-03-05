package com.cerbon.ml_template.forge;

import com.cerbon.ml_template.ModName;
import com.cerbon.ml_template.util.ModConstants;
import net.minecraftforge.fml.common.Mod;

@Mod(ModConstants.MOD_ID)
public class ModNameForge {

    public ModNameForge() {
        ModName.init();
    }
}