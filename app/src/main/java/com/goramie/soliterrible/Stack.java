package com.goramie.soliterrible;

import java.util.ArrayList;

public class Stack extends ArrayList<Card> {
    public Stack() {
        super();
    }

    public void draw() {
        // Draw the whole stack, with number shown on top passed in
    }

    public void dealFullDeck() {
        for (int type: Card.TYPES)
            for (int i = 0; i < 14; i++)
                super.add(new Card(type, i));
    }

    public Stack take() {
        Stack single = new Stack();
        single.add(super.remove(super.size()-1));
        return single;
    }
    public Stack take(int idx) {
        Stack moving = new Stack();
        for (int i=idx; i < super.size(); i++) {
            moving.add(super.remove(i));
        }
        return moving;
    }

    public Stack cardsShown() {
        Stack shown = new Stack();
        for (Card c: this) {
            if (c.isShowing()) shown.add(c);
        }
        return shown;
    }
}
