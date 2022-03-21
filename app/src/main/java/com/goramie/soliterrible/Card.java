package com.goramie.soliterrible;

public class Card {
    private int type;
    private int num;

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
}
