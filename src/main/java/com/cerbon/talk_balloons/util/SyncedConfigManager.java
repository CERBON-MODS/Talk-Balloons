package com.cerbon.talk_balloons.util;

import java.util.*;

public class SyncedConfigManager {
    private final Map<UUID, SynchronizedConfigData> configDatas = Collections.synchronizedMap(new HashMap<>());
    private final Set<UUID> defaultConfigDatas = Collections.synchronizedSet(new HashSet<>());

    public SynchronizedConfigData getPlayerConfig(UUID uuid) {
        return configDatas.computeIfAbsent(uuid, u -> {
            defaultConfigDatas.add(u);
            return SynchronizedConfigData.getDefault();
        });
    }

    public void setPlayerConfig(UUID uuid, SynchronizedConfigData configData) {
        configDatas.put(uuid, configData);
        defaultConfigDatas.remove(uuid);
    }

    public void removePlayerConfig(UUID uuid) {
        configDatas.remove(uuid);
        defaultConfigDatas.remove(uuid);
    }

    public void resetConfigs() {
        configDatas.clear();
        defaultConfigDatas.clear();
    }

    public void resetDefault() {
        defaultConfigDatas.clear();
    }
}
