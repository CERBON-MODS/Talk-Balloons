package com.cerbon.talk_balloons.client;

import com.cerbon.talk_balloons.util.SyncedConfigManager;

public class TalkBalloonsClient {
    public static final SyncedConfigManager syncedConfigs = new SyncedConfigManager();
    private static boolean hasServerSupport = false;

    public static void onClientDisconnect() {
        syncedConfigs.resetConfigs();
        hasServerSupport = false;
    }

    public static void enableServerSupport() {
        hasServerSupport = true;
    }

    public static boolean hasServerSupport() {
        return hasServerSupport;
    }
}
