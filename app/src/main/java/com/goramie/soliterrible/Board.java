package com.goramie.soliterrible;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.Collections;

public class Board extends Activity {
    private Stack[] tableau;// = new Stack[7];
    private Stack[] foundations;// = new Stack[4];
    private Stack[] deck;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.board);
        setUp();
        bind();
    }

    private void setUp() {
        deck = new Stack[2];
        for (int i = 0; i < deck.length; i++)
            deck[i] = new Stack(this);
        deck[0].dealFullDeck();
        Collections.shuffle(deck[0]);

        // Setup Tableau
        tableau = new Stack[7];
        for (int row = 0; row < tableau.length; row++) {
            tableau[row] = new Stack(this);
            System.out.println("Stack Created");
            for (int card = 0; card <= row; card++)
                tableau[row].add(deck[0].take());
            System.out.println("Deck Taken");
            tableau[row].get(tableau[row].size()-1).show();
        }

        // Setup Foundations
        foundations = new Stack[4];
        for (int row = 0; row < foundations.length; row++) {
            System.out.println("Foundation Created");
            foundations[row] = new Stack(this);
        }
    }

    private void bind() {
        LinearLayout deck_bind = findViewById(R.id.draw);
        for (Stack row: deck)
            deck_bind.addView(row.getLayout());
        LinearLayout tableau_bind = findViewById(R.id.tableau);
        for (Stack row: tableau)
            tableau_bind.addView(row.getLayout());
        LinearLayout foundations_bind = findViewById(R.id.foundations);
        for (Stack row: foundations)
            foundations_bind.addView(row.getLayout());
    }
}
