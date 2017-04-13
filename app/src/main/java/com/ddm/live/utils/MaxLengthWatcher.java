package com.ddm.live.utils;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by JiGar on 2016/11/23.
 */

public class MaxLengthWatcher implements TextWatcher {
    private int maxLen = 0;
    private EditText editText = null;
    private TextView textNum = null;


    public MaxLengthWatcher(EditText editText, TextView textNum, int maxLen) {
        this.editText = editText;
        this.textNum = textNum;
        this.maxLen = maxLen;
    }

    public void afterTextChanged(Editable arg0) {
        int strLen = arg0.length();
        if (strLen < maxLen) {
            textNum.setText(String.valueOf(strLen));
        } else {
            textNum.setText(String.valueOf(maxLen));
        }

    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        // TODO Auto-generated method stub
    }

    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        Editable editable = editText.getText();
        int len = editable.length();

        if (len > maxLen) {
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            //截取新字符串
            String newStr = str.substring(0, maxLen);
            editText.setText(newStr);
            editable = editText.getText();

            //新字符串的长度
            int newLen = editable.length();
            //旧光标位置超过字符串长度
            if (selEndIndex > newLen) {
                selEndIndex = editable.length();
            }
            //设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);
        }
    }

}
