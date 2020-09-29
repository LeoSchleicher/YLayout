package ru.schleicher.ylayout.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

/**
 * this is just YView with touch support
 */
public class Button extends YView {
	
	private static final String TAG = "Theme";
	
	private Boolean isInside = false;
	private Rect rect;
	
	public Button(Context context) {
		super(context);
	}
	
	public void onDown() {
		Log.d(TAG, "onDown");
	}
	
	public void onUp() {
		Log.d(TAG, "onUp");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			this.isInside = true;
			this.onDown();
			rect = new Rect(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
		} else if (action == MotionEvent.ACTION_UP) {
			this.onUp();
			if (this.isInside) {
				if (this.hasOnClickListeners()) {
					this.callOnClick();
				}
			}
		} else if (action == MotionEvent.ACTION_CANCEL) {
			this.isInside = false;
			this.onUp();
		} else if (action == MotionEvent.ACTION_MOVE) {
			if (!rect.contains(this.getLeft() + (int) event.getX(), this.getTop() + (int) event.getY())) {
				if (this.isInside) {
					this.onUp();
				}
				this.isInside = false;
			} else {
				if (!this.isInside) {
					this.onDown();
				}
				this.isInside = true;
			}
		}
		return true;
	}
	
}
