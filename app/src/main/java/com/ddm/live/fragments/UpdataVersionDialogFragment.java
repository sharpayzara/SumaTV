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
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.views.iface.IGetUpdateVersionView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/14.
 */
public class UpdataVersionDialogFragment extends DialogFragment {
    private IGetUpdateVersionView iGetUpdateVersionView;
    private String NEW_VERSION = "最新版本:";
    private String UPDATE_TIME = "更新时间:";

    public void attachFramgent(IGetUpdateVersionView iGetUpdateVersionView) {
        this.iGetUpdateVersionView = iGetUpdateVersionView;
    }

    @OnClick(R2.id.update_btn)
    public void update() {
        iGetUpdateVersionView.updateNewVersion();
    }

    @BindView(R2.id.close_btn)
    TextView closeBtn;

    @OnClick(R2.id.close_btn)
    public void close() {
        this.dismiss();
    }

    @BindView(R2.id.update_version_name)
    TextView updateVersionName;

    @BindView(R2.id.update_time)
    TextView updateTime;

    @BindView(R2.id.update_content)
    TextView updateContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.updataversion_layout, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (null != bundle) {
            String newVersionTitle = NEW_VERSION + bundle.getString("version");
            String updateTimeTitle = UPDATE_TIME + bundle.getString("updateTime");
            String updateContentText = bundle.getString("updateContent");
            String mustUpdate = bundle.getString("mustUpdate");
            updateVersionName.setText(newVersionTitle);
            updateTime.setText(updateTimeTitle);
            updateContent.setText(updateContentText);
            if (mustUpdate.equals(1)) {
                closeBtn.setVisibility(View.INVISIBLE);
            }
        }

        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if(getArguments()!=null && getArguments().getString("mustUpdate").equals("0")){
                        UpdataVersionDialogFragment.this.dismiss();
                    }
                }
                return true;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.UpdataVersionDialog) {/*设置MainDialogFragment的样式，这里的代码最好还是用我的，大家不要改动*/
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                getActivity().finish();
            }
        };
        return dialog;
    }

}
