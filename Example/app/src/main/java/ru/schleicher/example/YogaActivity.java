package ru.schleicher.example;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.yoga.android.YogaLayout;

import ru.schleicher.ylayout.YLayout;

public class YogaActivity extends AppCompatActivity {

    public YLayout root; // this holds tompost yoga-based element
    private int oldH = 0; // previous calculated width and height to catch layout events with dimension changes
    private int oldW = 0;
    private LayoutListener layoutListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set default layout listener
        this.setLayoutListener((width, height) -> {
            root.calculateLayout((float) width, (float) height);
            root.apply();
        });

        this.root = new YLayout(this);
        this.root.view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            View rv = findParent();
            if(rv != null){
                int newH = rv.getHeight();
                int newW = rv.getWidth();
                if(newH != oldH || newW != oldW){ // when new dimensions detected, recalculate yoga layouts
                    oldH = newH;
                    oldW = newW;
                    if(this.layoutListener != null){
                        this.layoutListener.onLayout(newW, newH);
                    }
                }
            }
        });

        this.addContentView(this.root.view, new YogaLayout.LayoutParams(YogaLayout.LayoutParams.MATCH_PARENT, YogaLayout.LayoutParams.MATCH_PARENT));
    }

    private View findParent(){
        ViewParent vp = this.root.view.getParent();
        while (vp != null){
            View v = (View) vp;
            if(v.getId() == android.R.id.content){
                return v;
            }
        }
        return null;
    }

    protected void setLayoutListener(LayoutListener layoutListener){
        this.layoutListener = layoutListener;
    }

    public interface LayoutListener {
        void onLayout(int width, int height);
    }

}
