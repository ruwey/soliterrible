package com.goramie.soliterrible;

import android.app.Activity;
import android.os.Bundle;

import java.util.Collections;

public class Board extends Activity {
    private Stack[] tableau = new Stack[7];
    private Stack[] foundations = new Stack[4];
    private Stack deck = new Stack();
    private Stack discard = new Stack();
    private int shownInDiscard = 3;

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
                tableau[row].addAll(deck.take());
            tableau[row].get(tableau[row].size()-1).toggleShow();
        }

        // Setup Foundations
        for (int row = 0; row < foundations.length; row++)
            foundations[row] = new Stack();
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
