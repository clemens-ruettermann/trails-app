package com.kenspeckle.trails.utils;

import android.content.Context;

public class MiscUtils {

	public static int convertDipToPixels(Context context, float dips) {
		return (int) (dips * context.getResources().getDisplayMetrics().density + 0.5f);
	}
}
