package com.ddm.live.models.layerinterfaces;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;

/**
 * Created by cxx on 2016/8/21.
 */
public interface MVInterfaces {
    void onNext(ResponseBaseInterface responseBaseInterface);

    void onError(String msg);
}
