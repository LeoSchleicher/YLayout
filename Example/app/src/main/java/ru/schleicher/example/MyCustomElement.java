package ru.schleicher.example;

import android.content.Context;
import android.graphics.Color;

import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaFlexDirection;
import com.facebook.yoga.YogaJustify;

import ru.schleicher.ylayout.LayoutUtils;
import ru.schleicher.ylayout.YLayout;

/**
 * Example of YLayout-based composite element
 */
public class MyCustomElement extends YLayout {

    private YLayout box1;
    private YLayout box2;
    private YLayout box3;

    public MyCustomElement(Context context) {
        super(context);
        this.setMinHeight(LayoutUtils.dp(50));
        this.view.setBackgroundColor(Color.GREEN);
        this.setFlexDirection(YogaFlexDirection.ROW);
        this.setAlignContent(YogaAlign.STRETCH);
        this.setJustifyContent(YogaJustify.SPACE_BETWEEN);
        this.setPadding(YogaEdge.HORIZONTAL, LayoutUtils.dp(10));

        this.box1 = new YLayout(context);
        this.box1.view.setBackgroundColor(Color.WHITE);
        this.appendChild(box1);
        this.box1.setWidth(LayoutUtils.dp(50));

        this.box2 = new YLayout(context);
        this.box2.view.setBackgroundColor(Color.WHITE);
        this.appendChild(box2);
        this.box2.setWidth(LayoutUtils.dp(50));

        this.box3 = new YLayout(context);
        this.box3.view.setBackgroundColor(Color.WHITE);
        this.appendChild(box3);
        this.box3.setWidth(LayoutUtils.dp(50));

    }



    /**
     * accessor to sub-elements
     * @return
     */
    public YLayout getLeftBox(){
        return this.box1;
    }


}
