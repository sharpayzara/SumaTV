package com.ddm.live.views.iface;

import com.ddm.live.models.bean.common.streamsbeans.SpecStreamBean;
import com.ddm.live.models.bean.common.streamsbeans.StreamsBean;

import java.util.List;

/**
 * Created by JiGar on 2016/1/15.
 */
public interface IListLiveView extends IBaseView{

    public void setInfo2List(List<StreamsBean> liveListItems,int currentPage, boolean hasPrevious,boolean isFirst);

    public void setInfo2MasterList(List<StreamsBean> liveListItems,int currentPage, boolean hasPrevious,boolean isFirst);

    public void intentPlayerActivity(SpecStreamBean playItem);

    public void deleteStreamResult(String rs);

}
