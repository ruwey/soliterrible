package com.goramie.soliterrible;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Stack {
    private ArrayList<Card> cards = new ArrayList<>();

    public Stack() { }
    public Stack(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void draw(int numShow) {
        // Draw the whole stack, with number shown on top passed in
    }
}
