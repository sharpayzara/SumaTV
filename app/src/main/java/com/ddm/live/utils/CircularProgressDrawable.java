package com.ddm.live.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

public class CircularProgressDrawable extends Drawable implements Animatable
{

    /** Drawable转圈的动画插值器。 */
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    /**
     * 圆弧臂长变化的动画插值器。<br/>
     * 该动画受 mModeAppearing 控制， 当 mModeAppearing 为 false
     * 的时候，圆弧的起始点在增加，圆弧的终止点不变，弧长在逐渐减少； 当 mModeAppearing 为 true 的时候，
     * 圆弧的起始点不变，圆弧的终止点变大，弧长在逐渐增加
     */
    private static final Interpolator SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    /** Drawable旋转动画的间隔，也就是多少毫秒圆弧转一圈，可以把该值扩大10倍来查看动画的慢动作。 */
    private static final int ANGLE_ANIMATOR_DURATION = 1200;
    /** 圆弧臂长变化一次的动画间隔，也就是臂长从最小到最大值的变化时间，也可以把该值扩大10倍来查看动画的慢动作。 */
    private static final int SWEEP_ANIMATOR_DURATION = 900;
    /** 圆弧的最小弧度。 */
    private static final int MIN_SWEEP_ANGLE = 30;
    /** 圆弧的最大弧度。 */
    private static final int MAX_SWEEP_ANGLE = 360 - MIN_SWEEP_ANGLE;

    /** 圆弧臂长变化的动画对象。 */
    private ValueAnimator mSweepAnimator;
    /** Drawable转圈的动画对象。 */
    private ValueAnimator mAngleAnimator;
    /** 圆弧臂长是否正在逐渐增加。 */
    private boolean mModeAppearing;
    /**
     * 圆弧起始位置的偏移量。<br/>
     * 每次臂长增加 、减少转换的时候， 圆弧起始位置的偏移量会增加 2倍的最小臂长
     */
    private float mCurrentGlobalAngleOffset;
    /** 当前Drawable旋转的角度。 */
    private float mCurrentGlobalAngle;
    /** 当前圆弧的弧度。 */
    private float mCurrentSweepAngle;
    /** 圆弧的宽度。 */
    private float mBorderWidth;
    /** 动画是否正在运行 */
    private boolean mRunning;

    /** 圆弧颜色变化的集合 */
    private int[] mColors;
    /** 当前圆弧颜色索引 */
    private int mCurrentColorIndex;
    /** 下一个圆弧颜色的索引 */
    private int mNextColorIndex = 1;

    private Paint mPaint;
    private final RectF fBounds = new RectF();

    public CircularProgressDrawable(int[] colors, float borderWidth)
    {
        mBorderWidth = borderWidth;

        if (colors == null)
        {
            mColors = new int[4];
            mColors[0] = Color.parseColor("#33B5E5");
            mColors[1] = Color.parseColor("#AA66CC");
            mColors[2] = Color.parseColor("#99CC00");
            mColors[3] = Color.parseColor("#FFBB33");
            mColors[3] = Color.parseColor("#FF4444");
        }
        else
        {
            mColors = colors;
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Cap.ROUND);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mColors[mCurrentColorIndex]);

        setupAnimations();
    }

    @Override
    public void draw(Canvas canvas)
    {
        float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
        float sweepAngle = mCurrentSweepAngle;
        if (mModeAppearing)
        {
            mPaint.setColor(gradient(mColors[mCurrentColorIndex], mColors[mNextColorIndex], mCurrentSweepAngle
                    / MAX_SWEEP_ANGLE));
        }
        else
        {
            startAngle = startAngle + sweepAngle - MIN_SWEEP_ANGLE;
            sweepAngle = 360 - sweepAngle;
        }
        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
    }

    @Override
    public void setAlpha(int alpha)
    {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf)
    {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    protected void onBoundsChange(Rect bounds)
    {
        super.onBoundsChange(bounds);
        fBounds.left = bounds.left + mBorderWidth / 2f + .5f;
        fBounds.right = bounds.right - mBorderWidth / 2f - .5f;
        fBounds.top = bounds.top + mBorderWidth / 2f + .5f;
        fBounds.bottom = bounds.bottom - mBorderWidth / 2f - .5f;
    }

    /** 根据进度，构建从color1到color2的中间颜色 */
    private static int gradient(int color1, int color2, float p)
    {
        int r1 = (color1 & 0xff0000) >> 16;
        int g1 = (color1 & 0xff00) >> 8;
        int b1 = color1 & 0xff;
        int r2 = (color2 & 0xff0000) >> 16;
        int g2 = (color2 & 0xff00) >> 8;
        int b2 = color2 & 0xff;
        int newr = (int) (r2 * p + r1 * (1 - p));
        int newg = (int) (g2 * p + g1 * (1 - p));
        int newb = (int) (b2 * p + b1 * (1 - p));
        return Color.argb(255, newr, newg, newb);
    }

    /** 初始化动画 */
    private void setupAnimations()
    {
        mAngleAnimator = ValueAnimator.ofFloat(360f);
        mAngleAnimator.setInterpolator(ANGLE_INTERPOLATOR);
        mAngleAnimator.setDuration(ANGLE_ANIMATOR_DURATION);
        mAngleAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAngleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAngleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                setCurrentGlobalAngle((Float) animation.getAnimatedValue());
            }
        });

        mSweepAnimator = ValueAnimator.ofFloat(MIN_SWEEP_ANGLE, MAX_SWEEP_ANGLE);
        mSweepAnimator.setInterpolator(SWEEP_INTERPOLATOR);
        mSweepAnimator.setDuration(SWEEP_ANIMATOR_DURATION);
        mSweepAnimator.setRepeatMode(ValueAnimator.RESTART);
        mSweepAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mSweepAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationRepeat(Animator animation)
            {
                toggleAppearingMode();
            }
        });
        mSweepAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                setCurrentSweepAngle((Float) animation.getAnimatedValue());
            }
        });
    }

    /**
     * 当mSweepAnimator完成一次后，设置当前mSweepAnimator的动作。并且在mSweepAnimator动作为增长时，
     * 变化圆弧颜色以及圆弧起始点的偏移量。
     */
    private void toggleAppearingMode()
    {
        mModeAppearing = !mModeAppearing;
        if (mModeAppearing)
        {
            mCurrentColorIndex = ++mCurrentColorIndex % mColors.length;
            mNextColorIndex = ++mNextColorIndex % mColors.length;
            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
        }
    }

    @Override
    public void start()
    {
        if (isRunning())
        {
            return;
        }
        mRunning = true;
        // 为了方便测试，可以注释掉下面两个动画中的一个，来
        // 分别查看每个独立的动画是如何运动的
        mAngleAnimator.start();
        mSweepAnimator.start();
        invalidateSelf();
    }

    @Override
    public void stop()
    {
        if (!isRunning())
        {
            return;
        }
        mRunning = false;
        mAngleAnimator.cancel();
        mSweepAnimator.cancel();
        invalidateSelf();
    }

    @Override
    public boolean isRunning()
    {
        return mRunning;
    }

    private void setCurrentGlobalAngle(float currentGlobalAngle)
    {
        mCurrentGlobalAngle = currentGlobalAngle;
        invalidateSelf();
    }

    private void setCurrentSweepAngle(float currentSweepAngle)
    {
        mCurrentSweepAngle = currentSweepAngle;
        invalidateSelf();
    }
}
