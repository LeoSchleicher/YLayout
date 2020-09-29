package ru.schleicher.example;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.facebook.yoga.YogaEdge;

import ru.schleicher.ylayout.LayoutUtils;
import ru.schleicher.ylayout.YLayout;
import ru.schleicher.ylayout.view.YView;

/**
 * this is an example of View-Based composite element
 */
public class MyCustomView extends YView {

    private TextView someLabel;

    public MyCustomView(Context context) {
        super(context);

        this.setBackgroundColor(Color.LTGRAY);

        this.someLabel = new TextView(context);
        this.someLabel.setText("some default text");

    }

    public TextView getLabel(){
        return this.someLabel;
    }

    /**
     * when this method is called, this view is already attached to some layout
     * ie this view is added as subview to another view.
     * but using this method is not mandatory, while getLayout() creates layout for this view automatically.
     * it may be aber useful, if this view must be a part of some specific YLayout
     */
    @Override
    public void onAttachToLayout() {
        super.onAttachToLayout();
        YLayout labelLayout = new YLayout(this.someLabel);
        this.getLayout().appendChild(labelLayout);
        this.getLayout().setPadding(YogaEdge.ALL, LayoutUtils.dp(10));
    }
}
