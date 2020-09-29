package ru.schleicher.ylayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.yoga.YogaDisplay;
import com.facebook.yoga.YogaMeasureFunction;
import com.facebook.yoga.YogaMeasureMode;
import com.facebook.yoga.YogaMeasureOutput;
import com.facebook.yoga.YogaNode;
import com.facebook.yoga.YogaNodeJNIFinalizer;

import java.util.ArrayList;
import java.util.List;

import ru.schleicher.ylayout.view.YView;

public class YLayout extends YogaNodeJNIFinalizer {

	private final static String TAG = "YLayout";

	public View view;
	protected List<YLayout> children = new ArrayList<>();

	private YAnimation animation;

	private int previousVisibility = -1; // this property holds visibility status

	/*
	 * Creates layout without view. Useful for Virtual Layouts, so it calculates only layout dimensions without applying them to some view
	 */
	public YLayout() {
		super();
	}

	/**
	 * Wraps some View with YLayout, all layout calculations will be applied to this view
	 *
	 * @param view
	 */
	public YLayout(View view) {
		this();
		this.view = view;
		if (this.view instanceof YView) {
			((YView) this.view).layout = this;
			((YView) this.view).onAttachToLayout();
		}
		// if attached view is TextView, create custom measure function
		if (this.view instanceof TextView && !this.isMeasureDefined()) {
			this.setMeasureFunction(new YLayout.ViewMeasureFunction(this.view));
		}
	}

	/**
	 * Creates automatically YView (RelativeLayout with some other possibilities) and wraps it
	 *
	 * @param context
	 */
	public YLayout(Context context) {
		this(new YView(context));
	}


	public void configure(YLayoutConfigurator configurator) {
		configurator.configure(this);
	}

	public void appendChild(YLayout child) {
		this.appendView(child.view);
		this.addChildAt(child, this.children.size()); // YogaNode method
		this.children.add(child); // add to children
	}


	public boolean isHidden() {
		return this.previousVisibility == View.GONE;
	}

	public void hide() {
		this.previousVisibility = View.GONE;
		if(this.view != null){
			this.view.setVisibility(View.GONE);
		}
	}

	public void show(){
		this.previousVisibility = View.VISIBLE;
		if(this.view != null){
			this.view.setVisibility(View.VISIBLE);
		}
	}

	public void removeChild(YLayout child){
		int pos = 0;
		boolean found = false;
		for (YLayout lo : this.children) {
			if(lo == child){
				found = true;
				if (lo.view != null) {
					((ViewGroup) lo.view.getParent()).removeView(lo.view);
				}
				lo.removeChildren(); // cascade to all subnodes
				break;
			}
			pos++;
		}
		// then remove node
		if(found){
			this.removeChildAt(pos);
			this.children.remove(child);
		}
	}

	// same as removeChild, but without cascading
	public void detachChild(YLayout child){
		int pos = 0;
		boolean found = false;
		for (YLayout lo : this.children) {
			if(lo == child){
				found = true;
				if (lo.view != null) {
					((ViewGroup) lo.view.getParent()).removeView(lo.view);
				}
				break;
			}
			pos++;
		}
		// then remove node
		if(found){
			this.removeChildAt(pos);
			this.children.remove(child);
		}
	}

	public void removeChildren() {
		// remove views
		for (YLayout lo : this.children) {
			if (lo.view != null) {
				((ViewGroup) lo.view.getParent()).removeView(lo.view);
			}
			lo.removeChildren(); // cascade
		}
		this.children = new ArrayList<>();
		// then remove yoganodes
		// WARN! Actually it recreates nodes if we begin from 1st node! do it in reverse order!
		for (int i = this.getChildCount(); i > 0; i--) {
			this.removeChildAt(i - 1);
		}
	}

	// same as removeChildren, but without cascading
	public void detachChildren() {
		// remove views
		for (YLayout lo : this.children) {
			if (lo.view != null) {
				((ViewGroup) lo.view.getParent()).removeView(lo.view);
			}
		}
		this.children = new ArrayList<>();
		// then remove yoganodes
		// WARN! Actually it recreates nodes if we begin from 1st node! do it in reverse order!
		for (int i = this.getChildCount(); i > 0; i--) {
			this.removeChildAt(i - 1);
		}
	}

	public List<YLayout> getChildren(){
		return this.children;
	}

	private void appendView(View v) {
		if (v != null) {
			if (this.view != null) {
				((ViewGroup) this.view).addView(v);
			} else {
				YogaNode parent = this.getParent();
				if (parent != null && parent instanceof YLayout) {
					((YLayout) parent).appendView(v);
				}
			}
		}
	}

	public void apply() {
		this.applyWithHeight(-1);
	}


	/**
	 * overriding calculated height, useful for overflow layouts
	 *
	 * @param height
	 */
	@SuppressLint("WrongConstant")
	public void applyWithHeight(int height) {
		if (this.view != null) {
			if(this.getDisplay() == YogaDisplay.NONE){
				// Log.d(TAG, "storing previous visibility: "+this.view.getVisibility());
				this.view.setVisibility(View.GONE);
				return; // so, all children are automatically hidden and there is no need to check them
			} else {
				if(this.previousVisibility >= 0){
					this.view.setVisibility(this.previousVisibility);
				} else {
					this.view.setVisibility(View.VISIBLE);
				}
			}
			ViewGroup.LayoutParams lp = this.view.getLayoutParams();
			if (lp == null) {
				lp = new RelativeLayout.LayoutParams(0, 0); // or MarginLayoutParams?
				this.view.setLayoutParams(lp);
				// Log.d("TAG", "just created layout params: " + lp);
			} else {
				// Log.d("TAG", "there is already LP: " + lp);
			}


			if (height > 0) {
				lp.height = height;
			} else {
				lp.height = (int) this.getLayoutHeight();
			}
			lp.width = (int) this.getLayoutWidth();
			if (lp instanceof ViewGroup.MarginLayoutParams) {
				((ViewGroup.MarginLayoutParams) lp).leftMargin = (int) this.getAccX();
				((ViewGroup.MarginLayoutParams) lp).topMargin = (int) this.getAccY();
			}

			this.view.requestLayout();
		}
		// cascade to childviews
		for (YLayout lo : this.children) {
			lo.apply();
		}
	}

	/**
	 * @return accumulated X position by virtual layouts
	 */
	public float getAccX() {
		YogaNode parent = this.getOwner();
		if (parent instanceof YLayout && ((YLayout) parent).view == null) {
			return ((YLayout) parent).getAccX() + this.getLayoutX();
		}
		return this.getLayoutX();
	}

	/**
	 * Same as getAccX() but for Y position
	 *
	 * @return
	 */
	public float getAccY() {
		YogaNode parent = this.getOwner();
		if (parent instanceof YLayout && ((YLayout) parent).view == null) {
			return ((YLayout) parent).getAccY() + this.getLayoutY();
		}
		return this.getLayoutY();
	}

	/**
	 * calculate layout and apply changes on child nodes
	 * !! not changing size of this element
	 */
	public void applyToChildren() {
		// Log.d("frm: ", "calculating layout for: "+this.getClass().getName()+" :: "+this.getLayoutWidth()+"x"+this.getLayoutHeight());
		this.calculateLayout(this.getLayoutWidth(), this.getLayoutHeight());
		this.apply();
	}

	public void applyToChildrenAnimated(int duration) {
		this.applyToChildrenAnimated(duration, new AccelerateInterpolator());
	}

	public void applyToChildrenAnimated(int duration, Interpolator interpolator) {
		this.applyToChildrenAnimated(duration, interpolator, true);
	}

	private void applyToChildrenAnimated(int duration, Interpolator interpolator, Boolean doCalc) {
		// create animators with current parameters
		if (this.view != null) {
			this.animation = new YAnimation(this); // saves current parameters to animation
			this.animation.setDuration(duration);
			this.animation.setInterpolator(interpolator);
		}
		for (YLayout cl : this.children) {
			cl.applyToChildrenAnimated(duration, interpolator, false);
		}
		if (doCalc) {
			this.startAnimation(doCalc);
		}
	}

	private void startAnimation(Boolean doCalc) {
		if (doCalc) {
			this.calculateLayout(this.getLayoutWidth(), this.getLayoutHeight());
		}
		if (this.view != null) {
			this.view.startAnimation(this.animation);
		}
		for (YLayout cl : this.children) {
			cl.startAnimation(false);
		}
	}


	/**
	 * Wrapper around measure function for yoga leaves.
	 */
	public static class ViewMeasureFunction implements YogaMeasureFunction {

		private View v;

		public ViewMeasureFunction(View v) {
			this.v = v;
		}


		/**
		 * A function to measure leaves of the Yoga tree.  Yoga needs some way to know how large
		 * elements want to be.  This function passes that question directly through to the relevant
		 * {@code View}'s measure function.
		 *
		 * @param node       The yoga node to measure
		 * @param width      The suggested width from the parent
		 * @param widthMode  The type of suggestion for the width
		 * @param height     The suggested height from the parent
		 * @param heightMode The type of suggestion for the height
		 * @return A measurement output ({@code YogaMeasureOutput}) for the node
		 */
		public long measure(YogaNode node, float width, YogaMeasureMode widthMode, float height, YogaMeasureMode heightMode) {
			final View view = this.v;
			if (view == null || view instanceof YView) {
				return YogaMeasureOutput.make(0, 0);
			}

			final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) width, viewMeasureSpecFromYogaMeasureMode(widthMode));
			final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) height, viewMeasureSpecFromYogaMeasureMode(heightMode));

			view.measure(widthMeasureSpec, heightMeasureSpec);
			// Log.d("TAG", "measured size: " + view.getMeasuredWidth() + "x" + view.getMeasuredHeight());

			return YogaMeasureOutput.make(view.getMeasuredWidth(), view.getMeasuredHeight());
		}

		private int viewMeasureSpecFromYogaMeasureMode(YogaMeasureMode mode) {
			if (mode == YogaMeasureMode.AT_MOST) {
				return View.MeasureSpec.AT_MOST;
			} else if (mode == YogaMeasureMode.EXACTLY) {
				return View.MeasureSpec.EXACTLY;
			} else {
				return View.MeasureSpec.UNSPECIFIED;
			}
		}
	}

	public YLayout getTopmostYLayout(){
		YLayout yl = this;
		while (true) {
			YLayout parent = yl.getParentYLayout();
			if(parent == null){
				break;
			}
			Log.d("frm:", "got parent in hierarchy: "+parent.getClass().getName());
			yl = parent;
		}
		return yl;
	}

	public YLayout getParentYLayout(){
		YogaNode parent = this.getOwner();
		if(parent instanceof YLayout){
			return (YLayout) parent;
		}
		return null;
	}

	public static interface YLayoutConfigurator {
		void configure(YLayout lo);
	}

}
