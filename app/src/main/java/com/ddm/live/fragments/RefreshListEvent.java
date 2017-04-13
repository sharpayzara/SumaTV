package com.ddm.live.fragments;

/**
 * Created by JiGar on 2016/11/1.
 */
public class RefreshListEvent {
    private int mode;//0表示刷新后定位到顶端，1表示刷新后定到之前的位置

    public RefreshListEvent(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
