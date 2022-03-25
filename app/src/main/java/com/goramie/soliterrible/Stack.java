package com.goramie.soliterrible;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collection;

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
        for (int type : Card.TYPES)
            for (int i = 0; i < 14; i++)
                this.add(new Card(c, type, i));
    }

    public Stack take() {
        Stack single = new Stack(c);
        l.removeView(this.get(this.size() - 1).getView());
        single.add(super.remove(super.size() - 1));
        return single;
    }

    public Stack take(int idx) {
        Stack moving = new Stack(c);
        for (int i = idx; i < super.size(); i++) {
            moving.add(super.remove(i));
        }
        return moving;
    }

    public Stack cardsShown() {
        Stack shown = new Stack(c);
        for (Card c : this) {
            if (c.isShowing()) shown.add(c);
        }
        return shown;
    }

    @Override
    public boolean add(Card c) {
        l.addView(c.getView());
        return super.add(c);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Card> c) {
        l.addView(c.ge);
        return super.addAll(c);
    }

    public LinearLayout getLayout() {
        return l;
    }
}
