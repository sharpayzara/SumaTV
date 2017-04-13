package com.ddm.live.ui;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by JiGar on 15/7/26.
 */
public abstract class Danmu extends TextView{
    private Context context;
    private int position;//弹幕的位置，在屏幕哪一行

    public Danmu(Context context) {
        super(context);
        this.context=context;
        setSingleLine();
        setPadding(5,5,5,5);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public abstract void send();


}
