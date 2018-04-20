package com.shu.tony.PlayTogether.nonentity.user;

import com.shu.tony.PlayTogether.nonentity.common.ResultBase;
import lombok.Data;

@Data
public class UserResult extends ResultBase {
    private long userOid;
    private String username;
    private String nickName;
}
