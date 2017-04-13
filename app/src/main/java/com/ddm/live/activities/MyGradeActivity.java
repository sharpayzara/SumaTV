package com.ddm.live.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.inject.components.AppComponent;
import com.squareup.picasso.Picasso;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ytzys on 2017/2/22.
 */
public class MyGradeActivity extends UserCenterBaseActivity {
    @BindView(R2.id.user_center_user_img)
    ImageView ivUser;

    @BindView(R2.id.pb_progressbar)
    ProgressBar pbProgressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygrade);
        ButterKnife.bind(this);
        rightTitle.setVisibility(View.GONE);
        title.setText("我的等级");
        Picasso.with(this)
                .load(((AppApplication) getApplication()).getAppUser().getSmallHeadimgurl())
                .placeholder(R.mipmap.user1)
                .error(R.mipmap.user1)
                .into(ivUser);
        handler.postDelayed(thread,600);

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            pbProgressbar.setProgress(msg.arg1);
            if(msg.arg1 > 50){
                //如果当i的值为100时将当前线程从handler中移除
                handler.removeCallbacks(thread);
            }else{
                handler.post(thread);
            }
        }
    };
    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }
    Runnable thread = new Runnable(){
        int i = 0;
        @Override
        public void run() {
         //   while (!Thread.currentThread().isInterrupted()) {
                Message message = handler.obtainMessage();
                i+=1;
                message.arg1 = i;

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
          //  }
            handler.sendMessage(message);
        }
    };
}
