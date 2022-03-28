package com.goramie.soliterrible;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.HashMap;
import java.util.Map;

public class Card extends AppCompatImageView {
    public static Map<Integer, String> TYPES;

    private Paint textPaint;

    private int type; // Even red, odd black
    private int num;
    private boolean showing = false;
    Stack parent;

    static {
        TYPES = new HashMap<>();
        TYPES.put(-2, "♥️");
        TYPES.put(-1, "♣️");
        TYPES.put(1, "♠️");
        TYPES.put(2, "♦️");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Card(Context c, int type, int num) {
        super(c);
        super.setImageResource(R.drawable.ic_card);
        super.setOnDragListener((view, dragEvent) -> {
            System.out.println("hello");

            return true;
        });

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(30);
        textPaint.setColor(0xff101010);

        this.type = type;
        this.num = num;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (showing)
            super.setImageResource(R.drawable.ic_showing_card);

        super.onDraw(canvas);

        if (showing)
            canvas.drawText(String.format("%s %s", TYPES.get(type), num), 30, 60, textPaint);
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
    public boolean toggleShow() {
        showing = !showing;
        return showing;
    }
    public void setParent(Stack s) {
        parent = s;
    }
}