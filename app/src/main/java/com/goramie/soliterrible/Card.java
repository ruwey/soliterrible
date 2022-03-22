package com.goramie.soliterrible;

public class Card {
    public static final int[] TYPES = {-2, -1, 1, 2};

    private int type;
    private int num;
    private boolean showing = false;

    public Card(int type, int num) {
        this.type = type;
        this.num = num;
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
}
