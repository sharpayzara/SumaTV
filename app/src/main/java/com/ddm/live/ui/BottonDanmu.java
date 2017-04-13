package com.ddm.live.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by JiGar on 15/7/27.
 */
public class BottonDanmu extends Danmu {
    private OnDisappearListener onDisappearListener;
    private int duration;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                if(onDisappearListener!=null)
                {
                    onDisappearListener.disappear();
                }
            }
        }
    };

    public BottonDanmu(Context context, int duration) {
        super(context);
        this.duration=duration;
    }

    public interface OnDisappearListener
    {
        public void disappear();
    }
    @Override
    public void send() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(duration);
                    handler.sendEmptyMessage(1);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setOnDisappearListener(OnDisappearListener onDisappearListener )
    {
        this.onDisappearListener=onDisappearListener;
    }
}
