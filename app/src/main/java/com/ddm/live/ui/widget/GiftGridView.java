package com.ddm.live.ui.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by zhangjx on 2017/2/28.
 */
public class GiftGridView extends GridView {

    public GiftGridView(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                return false;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
