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
    private int shownInDiscard = 3;

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

    public boolean checkWin() {
        for (Stack s: tableau) {
            if (s.get(s.size() - 1).getNum() != 13)
                return false;
        }
        return true;
    }

    public boolean checkAddFoundation(Card c, int row) {
        Card last = tableau[row].get(tableau[row].size() - 1);
        boolean sameType = (c.getType() == last.getType());
        boolean ascending = (c.getNum() > last.getNum());
        return sameType && ascending;
    }

    public boolean checkAddRow(Card c, int row) {
        Card last = tableau[row].get(tableau[row].size() - 1);
        boolean opposite = ((c.getType() % 2) != (last.getType() % 2));
        boolean descending = (c.getNum() < last.getNum());
        return opposite && descending;
    }

    public void flipToDiscard() {
        if (deck.size() == 0) {
            // no animation for reset deck, automatic
            Collections.reverse(discard);
            deck.addAll(discard);
        }

        if (deck.size() > 2) {
            discard.addAll(deck.take(deck.size() - 3));
        } else  {
            discard.addAll(deck);
        }

        // we add to the end of the discard and remove from the beginning of the ones shown
        while (discard.cardsShown().size() > shownInDiscard) {
            discard.get(discard.indexOf(discard.cardsShown().get(0))).toggleShow();
        }
    }
}
