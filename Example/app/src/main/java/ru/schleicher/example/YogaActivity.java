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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.root = new YLayout(this);
        this.root.view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View rv = findParent();
                if(rv != null){
                    int newH = rv.getHeight();
                    int newW = rv.getWidth();
                    if(newH != oldH || newW != oldW){ // when new dimensions detected, recalculate yoga layouts
                        oldH = newH;
                        oldW = newW;
                        root.calculateLayout((float) newW, (float) newH);
                        root.apply();
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

}
