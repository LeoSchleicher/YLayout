package ru.schleicher.ylayout;

import android.content.Context;

import ru.schleicher.ylayout.view.Button;

public class YButton extends YLayout {

    public YButton(Context context){
        super();
        Button b = new Button(context);
        this.view = b;
        b.layout = this;
        // call onAttachToLayout here?
    }

    public YButton(Button button) {
        super();
        this.view = button;
        button.layout = this;
        button.onAttachToLayout();
    }

    public Button getButton(){
        return (Button) this.view;
    }


}
