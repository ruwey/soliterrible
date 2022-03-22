package com.goramie.soliterrible;

import android.app.Activity;
import android.os.Bundle;

import java.util.Collections;

public class Board extends Activity {
    private Stack[] tableau = new Stack[7];
    private Stack[] foundations = new Stack[4];
    private Stack deck = new Stack();
    private Stack discard = new Stack();

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setUp();
        setContentView(R.layout.game);

    }

    private void setUp() {
        deck.dealFullDeck();
        Collections.shuffle(deck);

        // Setup Tableau
        for (int row = 0; row < tableau.length; row++) {
            tableau[row] = new Stack();
            for (int card = 0; card <= row; card++)
                tableau[row].add(deck.take());
            tableau[row].get(tableau[row].size()-1).show();
        }

        // Setup Foundations
        for (int row = 0; row < foundations.length; row++)
            foundations[row] = new Stack();
    }
}
