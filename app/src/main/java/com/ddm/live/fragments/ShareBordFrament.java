package com.ddm.live.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.constants.Constants;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/21.
 */

public class ShareBordFrament extends DialogFragment {
    private UMImage image;
    private String name;
    private String avatarSmall;
    private String topic;
    private int qnID;

    @OnClick(R.id.butter_wechat_login_btn)
    public void wechatShare() {
        share(SHARE_MEDIA.WEIXIN);
    }

    @OnClick(R.id.butter_we_circle_btn)
    public void friendCircle() {
        share(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    @OnClick(R.id.butter_qq_login_btn)
    public void qqShare() {
        share(SHARE_MEDIA.QQ);
    }

    @OnClick(R.id.butter_qq_qzone_btn)
    public void qzoneShare() {
        share(SHARE_MEDIA.QZONE);
    }

    @OnClick(R.id.butter_weibo_login_btn)
    public void weiboShare() {
        share(SHARE_MEDIA.SINA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_bord_layout, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        avatarSmall = bundle.getString("avatarSmall", "");
        topic=bundle.getString("topic","");
        name = bundle.getString("name", "");
        qnID = bundle.getInt("qnId", 0);
        image = new UMImage(getActivity(), avatarSmall);
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    ShareBordFrament.this.dismiss();
                }
                return true;
            }
        });
        this.getDialog().getWindow().getAttributes().windowAnimations=R.style.umeng_socialize_dialog_animations;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareBordFrament.this.dismiss();
            }
        });
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.ShareBorDDialog) {/*设置MainDialogFragment的样式，这里的代码最好还是用我的，大家不要改动*/
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                getActivity().finish();
            }
        };
        return dialog;
    }

    public void share(SHARE_MEDIA shareMedia) {
        new ShareAction(getActivity()).setPlatform(shareMedia).setCallback(umShareListener)
                .withMedia(image)
                .withTitle(Constants.SHARE_TITLE)
                .withText(name + getResources().getString(R.string.xiaozhu_share_pre_title) + topic + getResources().getString(R.string.xiaozhu_share_after_title))
                .withTargetUrl(Constants.SHARE_SERVER_URL + qnID + "/")
                .share();
        ShareBordFrament.this.dismiss();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(AppApplication.getInstance(),"分享成功",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(AppApplication.getInstance(),"分享失败",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(AppApplication.getInstance(),"取消分享",Toast.LENGTH_SHORT).show();
        }
    };
}
