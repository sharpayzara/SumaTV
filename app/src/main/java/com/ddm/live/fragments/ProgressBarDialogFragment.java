package com.ddm.live.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddm.live.R;

/**
 * Created by Administrator on 2016/11/5.
 */

public class ProgressBarDialogFragment extends DialogFragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progressbar_dialog_fragment, container, false);
         this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
             @Override
             public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                 if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction()== KeyEvent.ACTION_UP)
                 {
                     ProgressBarDialogFragment.this.dismiss();
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
