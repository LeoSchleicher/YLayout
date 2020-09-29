package ru.schleicher.example;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaFlexDirection;

import ru.schleicher.ylayout.LayoutUtils;
import ru.schleicher.ylayout.YLayout;


public class MainActivity extends YogaActivity {

    @SuppressLint({"ShowToast", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // configure root view
        this.root.configure(lo -> {
            lo.setFlexDirection(YogaFlexDirection.COLUMN);
            lo.setPadding(YogaEdge.TOP, LayoutUtils.dp(50));
        });
        this.root.view.setBackgroundColor(Color.BLACK);

        YLayout bluebox = new YLayout(this); // ylayout with given context creates view automatically
        bluebox.view.setBackgroundColor(Color.BLUE);
        root.appendChild(bluebox);
        bluebox.configure(lo -> {
            lo.setFlexDirection(YogaFlexDirection.ROW);
            lo.setAlignItems(YogaAlign.CENTER);
            lo.setPadding(YogaEdge.ALL, LayoutUtils.dp(10)); // padding 10 dp
            lo.setMinHeight(LayoutUtils.dp(100));
        });

        YLayout yellowbox = new YLayout(this);
        bluebox.appendChild(yellowbox);
        yellowbox.view.setBackgroundColor(Color.YELLOW);
        yellowbox.configure(lo -> {
            lo.setWidth(LayoutUtils.dp(40));
            lo.setHeight(LayoutUtils.dp(40));
        });

        YLayout redBox = new YLayout(this);
        bluebox.appendChild(redBox);
        redBox.view.setBackgroundColor(Color.RED);
        redBox.configure(lo -> {
            lo.setWidth(LayoutUtils.dp(30));
            lo.setHeightPercent(80);
        });

        YLayout spacer = new YLayout(); // empty constructor creates layout without view. it will be calculated, but without view rendering, this is useful to align other elements
        bluebox.appendChild(spacer);
        spacer.setFlex(1);

        YLayout whiteBox = new YLayout(this);
        bluebox.appendChild(whiteBox);
        whiteBox.view.setBackgroundColor(Color.WHITE);
        whiteBox.configure(lo -> {
            lo.setWidth(LayoutUtils.dp(50));
            lo.setHeight(LayoutUtils.dp(40));
        });


        YLayout button = new YLayout(new Button(this)); // YLayout constructor with view as argument, it wraps given view and applies all layout calculations on it
        ((Button)button.view).setText("click me!");
        ((Button)button.view).setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "oh no, I'm clicked!", Toast.LENGTH_LONG);
        });
        button.setHeight(LayoutUtils.dp(40));
        button.setMargin(YogaEdge.TOP, LayoutUtils.dp(10));
        root.appendChild(button);


        MyCustomElement mce = new MyCustomElement(this); // MyCustomElement is an YLayout-based composite view
        root.appendChild(mce);
        mce.setMargin(YogaEdge.TOP, LayoutUtils.dp(10)); // we can customize it's default properties

    }

}