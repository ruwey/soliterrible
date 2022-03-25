package com.goramie.soliterrible;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Card {
    public static final int[] TYPES = {-2, -1, 1, 2};

    private int type;
    private int num;
    private boolean showing = false;

    private ImageView view;

    public Card(Context c, int type, int num) {
        this.type = type;
        this.num = num;

        view = new ImageView(c);
        view.setImageResource(R.drawable.ic_card);
        view.setAdjustViewBounds(false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200));
    }

    public int getType() {
        return type;
    }
    public int setType(int type) {
        int temp = this.type;
        this.type = type;
        return temp;
    }
    public int getNum() {
        return num;
    }
    public int setNum(int num) {
        int temp = this.num;
        this.num = num;
        return temp;
    }
    public boolean isShowing() {
        return showing;
    }
    public void show() {
        showing = true;
    }
    public ImageView getView() {
        return view;
    }
}
