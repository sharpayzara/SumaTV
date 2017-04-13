package com.ddm.live.utils.camera.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

public class DisplayUtil {
	private static final String TAG = "DisplayUtil";
	/**
	 * dip转px
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}

	/**
	 * px转dip
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}

	/**
	 * 获取屏幕宽度和高度，单位为px
	 * @param context
	 * @return
	 */
	public static Point getScreenMetrics(Context context){
		DisplayMetrics dm =context.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
		Log.i(TAG, "Screen---Width = " + w_screen + " Height = " + h_screen + " densityDpi = " + dm.densityDpi);
		return new Point(w_screen, h_screen);

	}

	/**
	 * 获取屏幕长宽比
	 * @param context
	 * @return
	 */
	public static float getScreenRate(Context context){
		Point P = getScreenMetrics(context);
		float H = P.y;
		float W = P.x;
		return (H/W);
	}

	//获取屏幕原始尺寸高度，包括虚拟功能键高度
	public static int getDeviceScreenHeight(Context context){
		int dpi = 0;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
			method.invoke(display, displayMetrics);
			dpi=displayMetrics.heightPixels;
		}catch(Exception e){
			e.printStackTrace();
		}
		return dpi;
	}

	/**
	 * 获取 虚拟按键的高度
	 * @param context
	 * @return
	 */
	public static  int getBottomStatusHeight(Context context){
		int totalHeight = getDeviceScreenHeight(context);

		int contentHeight = getScreenHeight(context);

		return totalHeight  - contentHeight;
	}

	/**
	 * 标题栏高度
	 * @return
	 */
	public static int getTitleHeight(Activity activity){
		return  activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
	}

	/**
	 * 获得状态栏的高度
	 *
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context)
	{

		int statusHeight = -1;
		try
		{
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return statusHeight;
	}


	/**
	 * 获得屏幕高度
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context)
	{
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 获取屏幕宽度
	 */
	public static int getScreenW(Context aty) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = aty.getResources().getDisplayMetrics();
		int w = dm.widthPixels;
		// int w = aty.getWindowManager().getDefaultDisplay().getWidth();
		return w;
	}

	//获取真实宽高
	public static int[] getScreentHeight(Activity activity) {
		int heightPixels;
		int widthPixels;
		WindowManager w = activity.getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		heightPixels = metrics.heightPixels;
		widthPixels = metrics.widthPixels;
		// includes window decorations (statusbar bar/navigation bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
			try {
				heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
				widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
			} catch (Exception ignored) {
			}
			// includes window decorations (statusbar bar/navigation bar)
		else if (Build.VERSION.SDK_INT >= 17)
			try {
				android.graphics.Point realSize = new android.graphics.Point();
				Display.class.getMethod("getRealSize",
						android.graphics.Point.class).invoke(d, realSize);
				heightPixels = realSize.y;
				widthPixels = realSize.x;
			} catch (Exception ignored) {
			}
		int[] realWidthAndHeightSize = {widthPixels, heightPixels};
		return realWidthAndHeightSize;
	}
}
