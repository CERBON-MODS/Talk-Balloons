package com.cerbon.ml_template.fabric;

import com.cerbon.ml_template.ModName;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class ModNameFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        ModName.init();
    }

    @Override
    public void onInitializeClient() {}
}