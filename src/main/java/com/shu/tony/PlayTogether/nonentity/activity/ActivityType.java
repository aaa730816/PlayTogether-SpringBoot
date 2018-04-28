package com.shu.tony.PlayTogether.nonentity.activity;

public enum ActivityType {
    BASKETBALL("basketball"), FOOTBALL("football"), TABLETENNIS("table-tennis"),
    TENNIS("tennis"), CHESS("chess"), RECREATION("recreation"), GAME("game"),
    MEAL("meal"), OTHER("other");

    ActivityType(String type) {
        this.type = type;
    }

    String type;

    String getType() {
        return this.type;
    }
}
