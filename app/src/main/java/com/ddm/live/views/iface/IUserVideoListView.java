package com.ddm.live.views.iface;

import com.ddm.live.models.bean.live.LiveItem;
import com.ddm.live.models.bean.live.PlayItem;

import java.util.ArrayList;

/**
 * Created by JiGar on 2016/1/15.
 */
public interface IUserVideoListView extends IBaseView{

//    public void setInfo2List(ArrayList<LiveItem> liveListItems, boolean isFirst);

    public void userListIntentPlayerActivity(boolean result, PlayItem playItem);

    public void userListDeleteStreamResult(String rs);

}
