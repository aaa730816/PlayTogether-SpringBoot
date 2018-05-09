package com.shu.tony.PlayTogether.nonentity.common;

public enum EventType {
    ACTIVITY("activity"),EQUIPMENT("equipment");
    String name;

    EventType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }
}
