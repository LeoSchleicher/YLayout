package ru.schleicher.ylayout.view;

import android.content.Context;
import android.widget.RelativeLayout;

import ru.schleicher.ylayout.YLayout;

/**
 * Created by leonidschleicher on 28.11.17.
 */

public class YView extends RelativeLayout {
	
	public YLayout layout;
	
	public YView(Context context) {
		super(context);
	}
	
	/**
	 * here layout is already defined, so we can use it
	 * override this method in composite views to attach layouts to it
	 */
	public void onAttachToLayout() {

	}
	
	/**
	 * Wraps current view with new layout or gets layout back if already defined
	 * @return @YLayout
	 */
	public YLayout getLayout(){
		if(this.layout == null){
			this.layout = new YLayout(this);
		}
		return this.layout;
	}
	
}
