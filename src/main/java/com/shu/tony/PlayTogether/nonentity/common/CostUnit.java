package com.shu.tony.PlayTogether.nonentity.common;

public enum CostUnit {
    DAY(24*60),HOUR(60),MINUTE(1);
    private int mul;
    CostUnit(int mul) {
        this.mul = mul;
    }
    public int getMul() {
        return mul;
    }
}
