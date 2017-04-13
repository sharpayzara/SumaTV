package com.ddm.live.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.ddm.live.R;

public class CircularProgress extends View
{
    private static final int DEFAULT_BORDER_WIDTH = 3;

    private CircularProgressDrawable mDrawable;

    public CircularProgress(Context context)
    {
        this(context, null);
    }

    public CircularProgress(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CircularProgress(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        float density = context.getResources().getDisplayMetrics().density;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularProgress, defStyleAttr, 0);
        float borderWidth = a.getDimension(R.styleable.CircularProgress_progressBarBorderWidth, DEFAULT_BORDER_WIDTH * density);
        a.recycle();

        int[] colors = new int[4];
        colors[0] = context.getResources().getColor(R.color.red);
        colors[1] = context.getResources().getColor(R.color.yellow);
        colors[2] = context.getResources().getColor(R.color.green);
        colors[3] = context.getResources().getColor(R.color.blue);

        mDrawable = new CircularProgressDrawable(colors, borderWidth);
        mDrawable.setCallback(this);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        mDrawable.draw(canvas);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility)
    {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE)
        {
            mDrawable.start();
        }
        else
        {
            mDrawable.stop();
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who)
    {
        return who == mDrawable || super.verifyDrawable(who);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawable.setBounds(0, 0, w, h);
    }
}
