package com.ddm.live.fragments;


import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddm.live.R;
import com.ddm.live.constants.Constants;
import com.ddm.live.views.iface.IOnTouchView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cxx on 2016/10/14.
 */
public class InputDialogFragment extends DialogFragment {
    private IOnTouchView iOnTouchView;
    private TextView sendInput;
    public static boolean isSwitchChecked;
    @BindView(R.id.llinputparent)
    LinearLayout llinputParent;
//    @BindView(R.id.etInput)
    EditText editText;
    @BindView(R.id.switch_danmu)
    ImageView switchButton;
    private String tag;
    public void attachFramgent(IOnTouchView iOnTouchView) {
        this.iOnTouchView = iOnTouchView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_dialog_fragment, container, false);
        ButterKnife.bind(this, view);
        editText=null;
        editText=(EditText)view.findViewById(R.id.etInput);
         tag=this.getTag();
        editText.setText(tag);
        editText.setSelection(tag.length());
        isSwitchChecked = false;
//        switchButton.setChecked(isSwitchChecked);
//        switchButton.setText("弹幕", "弹幕");
//        switchButton.setTextColor(createColorStateList(0x204F2D54, 0xff000000));
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSwitchChecked) {
                    switchButton.setImageResource(R.mipmap.barrage_btn_selected);
                    editText.setHint("开启弹幕消息 1钻石/条");
                    isSwitchChecked = true;
                } else {
                    switchButton.setImageResource(R.mipmap.barrage_btn_nor);
                    editText.setHint("在此输入你要说的话");
                    isSwitchChecked = false;
                }
            }
        });
//        softKeyboardListnenr();
        return view;
    }

    public ColorStateList createColorStateList(int normal, int checked) {
        int[] color = new int[]{normal, checked, normal};
        int[][] states = new int[3][];
        states[0] = new int[]{-android.R.attr.state_checked};
        states[1] = new int[]{android.R.attr.state_checked};
        states[2] = new int[]{};
        return new ColorStateList(states, color);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                llinputParent.setVisibility(View.GONE);
                InputDialogFragment.this.dismiss();
            }
        });
        sendInput = (TextView) view.findViewById(R.id.sendInput);
        sendInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dmContent = editText.getText().toString();
                if (dmContent.isEmpty()) return;
                if (isSwitchChecked) {
                    iOnTouchView.sendLiveUserMesg(dmContent, Constants.MSG_TYPE_DANMU);
                } else {
                    iOnTouchView.sendLiveUserMesg(dmContent, Constants.MSG_TYPE_NORMAL);
                }
                editText.setText("");
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.InputDialog) {/*设置MainDialogFragment的样式，这里的代码最好还是用我的，大家不要改动*/
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                getActivity().finish();
            }
        };
        return dialog;
    }
}
