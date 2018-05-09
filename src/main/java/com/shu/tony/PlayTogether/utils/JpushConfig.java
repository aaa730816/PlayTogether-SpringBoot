package com.shu.tony.PlayTogether.utils;

public enum JpushConfig {
    URL("https://bjapi.push.jiguang.cn/v3/push"),APP_KEY("2a2ded96475e6c7618c5f5f1"),MASTER_SECRET("29099a9b1ad8d71ccbe16665");
    String config;
    JpushConfig(String config) {
        this.config = config;
    }

    public String getConfig() {
        return config;
    }
}
