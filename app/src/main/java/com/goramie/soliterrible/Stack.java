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

    public Card take() { // this should also be implemented to take all after a card
        return super.remove(super.size()-1);
    }
}
