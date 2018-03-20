package com.shu.tony.PlayTogether.nonentity.user;

public enum LoginType {
    QQ("QQ"),CMN("CMN");

    LoginType(String type) {
        this.type=type;
    }
    String type;

    public String getType() {
        return type;
    }
}
