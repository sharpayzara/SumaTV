package com.ddm.live.views.iface;

import com.ddm.live.models.bean.common.giftbeans.GiftData;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */

public interface IGiftListView extends IBaseView {
    /**
     * 获取礼物列表
     */
    public void getGiftList(List<GiftData> giftDataList);
}
