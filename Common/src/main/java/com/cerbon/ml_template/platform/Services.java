package com.cerbon.ml_template.platform;

import com.cerbon.ml_template.util.ModConstants;

import java.util.ServiceLoader;

public class Services {

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        ModConstants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
