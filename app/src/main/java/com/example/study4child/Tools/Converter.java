package com.example.study4child.Tools;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;

public class Converter {
	public static int Pixels(Context context, float dps) {
		return (int) (dps * context.getResources().getDisplayMetrics().density);
	}
	
	public static float Dps(Context context, int pxs) {
		return (float) pxs / context.getResources().getDisplayMetrics().density;
	}
}