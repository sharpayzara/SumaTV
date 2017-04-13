package com.ddm.live.fragments;

/**
 * Created by Administrator on 2017/1/16.
 */

public class RefreshSelectedListEvent {
    private int mode;//0表示刷新后定位到顶端，1表示刷新后定到之前的位置

    public RefreshSelectedListEvent(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
