package ru.schleicher.ylayout;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by leonidschleicher on 13.12.17.
 */


public class YTextView extends YLayout {
	
	public YTextView(Context context) {
		super();
		this.view = new TextView(context);
		if(!this.isMeasureDefined()){
			this.setMeasureFunction(new YLayout.ViewMeasureFunction(this.view));
		}
	}
	
	public TextView getTextView(){
		return (TextView)this.view;
	}
	
	public void setText(CharSequence text){
		this.getTextView().setText(text);
	}

}
