package com.shu.tony.PlayTogether.nonentity.user;

import lombok.Data;

@Data
public class NickNameVo {
    private String userOid;
    private String nickName;

    public NickNameVo(String userOid, String nickName) {
        this.userOid = userOid;
        this.nickName = nickName;
    }

    public NickNameVo() {
    }
}
