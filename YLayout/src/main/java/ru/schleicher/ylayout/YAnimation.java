package ru.schleicher.ylayout;

import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.lang.ref.WeakReference;

public class YAnimation extends Animation {
	private WeakReference<YLayout> layout;
	
	private int w, h, x, y;
	private int w1, h1, x1, y1;
	
	public YAnimation(YLayout lo) {
		this.layout = new WeakReference<>(lo);
		this.w = layout.get().view.getLayoutParams().width;
		this.h = layout.get().view.getLayoutParams().height;
		if(layout.get().view.getLayoutParams() instanceof  ViewGroup.MarginLayoutParams) {
			this.x = ((ViewGroup.MarginLayoutParams) layout.get().view.getLayoutParams()).leftMargin;
			this.y = ((ViewGroup.MarginLayoutParams) layout.get().view.getLayoutParams()).topMargin;
		}
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		boolean needAnimate = false;
		if (w != w1) {
			layout.get().view.getLayoutParams().width = w + (int) (interpolatedTime * (w1 - w));
			needAnimate = true;
		}
		if (h != h1) {
			layout.get().view.getLayoutParams().height = h + (int) (interpolatedTime * (h1 - h));
			needAnimate = true;
		}
		if (y != y1) {
			((ViewGroup.MarginLayoutParams) layout.get().view.getLayoutParams()).topMargin = y + (int) (interpolatedTime * (y1 - y));
			needAnimate = true;
		}
		if (x != x1) {
			((ViewGroup.MarginLayoutParams) layout.get().view.getLayoutParams()).leftMargin = x + (int) (interpolatedTime * (x1 - x));
			needAnimate = true;
		}
		/*
		view.getLayoutParams().height = (int) (interpolatedTime);
		*/
		// if(needAnimate) {
		layout.get().view.requestLayout();
		//}
	}
	
	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		this.w1 = (int) this.layout.get().getLayoutWidth();
		this.h1 = (int) this.layout.get().getLayoutHeight();
		this.x1 = (int) this.layout.get().getAccX();
		this.y1 = (int) this.layout.get().getAccY();
		Log.d("TAG", "initialize ! " + w + "x" + h + " [" + x + ":" + y + "] -> " + w1 + "x" + h1 + " [" + x1 + ":" + y1 + "]");
	}
	
	@Override
	public boolean willChangeBounds() {
		return true;
	}
}
