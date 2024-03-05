package com.cerbon.talk_balloons.platform;

import com.cerbon.talk_balloons.util.TBConstants;

import java.util.ServiceLoader;

public class Services {

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        TBConstants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
