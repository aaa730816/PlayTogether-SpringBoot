package com.shu.tony.PlayTogether.nonentity.message;

import com.shu.tony.PlayTogether.nonentity.activity.ActivityVo;
import com.shu.tony.PlayTogether.nonentity.equipment.EquipmentVo;
import com.shu.tony.PlayTogether.nonentity.user.NickNameVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class EventSummary {
    private EquipmentVo equipment;
    private ActivityVo activity;
    private List<NickNameVo> member = new ArrayList<>();
}
