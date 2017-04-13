package com.ddm.live.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.ddm.live.R;

/**
 * Created by JiGar on 2016/10/30.
 */
public class EditTextWithDel extends EditText {
    private final static String TAG = "EditTextWithDel";
    private Drawable imgInable;
    private Drawable imgAble;
    private Drawable imgLine;
    private Context mContext;

    public EditTextWithDel(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        imgInable = mContext.getResources().getDrawable(R.mipmap.delete_gray);
        imgAble = mContext.getResources().getDrawable(R.mipmap.delete);
        imgLine = mContext.getResources().getDrawable(R.drawable.xz_change_info_editor);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    //设置删除图片
    private void setDrawable() {
        if (length() < 1)
//            setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
        {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgLine);
            setPadding(20, 8, 20, 10);
        } else
//            setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
        {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgLine);
            setPadding(20, 8, 20, 10);
        }
    }

    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
//            Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 50;
            if (rect.contains(eventX, eventY))
                setText("");
        }*/
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}