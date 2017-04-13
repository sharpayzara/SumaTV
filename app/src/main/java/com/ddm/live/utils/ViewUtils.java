package com.ddm.live.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JiGar on 2017/1/13.
 */

public class ViewUtils {

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
