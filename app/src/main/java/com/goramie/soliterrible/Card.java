package com.goramie.soliterrible;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.graphics.Typeface;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.HashMap;
import java.util.Map;

public class Card extends AppCompatImageView {
    public static Map<Integer, String> TYPES;
    public static Map<Integer, String> SUITS;

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

        SUITS = new HashMap<>();
        SUITS.put(11, "J");
        SUITS.put(12, "Q");
        SUITS.put(13, "K");
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
        textPaint.setTextSize(45);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(0xff101010);
        textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        this.type = type;
        this.num = num;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (showing)
            super.setImageResource(R.drawable.ic_showing_card);

        super.onDraw(canvas);

        if (showing) {
            String txt = String.format("%s %s", TYPES.get(type), num > 10 ? SUITS.get(num) : num);
            int y = (int) (canvas.getHeight() / 2 + textPaint.getTextSize() / 4 + 0.5);
            int x = (int) ((canvas.getWidth()) / 2 + 0.5);
            canvas.drawText(txt, x, y, textPaint);
        }
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