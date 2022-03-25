package com.goramie.soliterrible;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class Stack extends ArrayList<Card> {
    private Context c;
    private LinearLayout l;

    public Stack(Context c) {
        super();

        this.c = c;
        l = new LinearLayout(this.c);
        l.setOrientation(LinearLayout.VERTICAL);
    }

    public void dealFullDeck() {
        for (int type: Card.TYPES)
            for (int i = 0; i < 14; i++)
                this.add(new Card(c, type, i));
    }

    public Card take() { // this should also be implemented to take all after a card
        l.removeView(this.get(this.size()-1).getView());
        return super.remove(super.size()-1);
    }

    @Override
    public boolean add(Card c) {
        l.addView(c.getView());
        return super.add(c);
    }
    public LinearLayout getLayout() {
        return l;
    }
}
