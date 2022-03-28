package com.goramie.soliterrible;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.annotation.RequiresApi;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Board extends Activity {
    private Stack[] tableau;// = new Stack[7];
    private Stack[] foundations;// = new Stack[4];
    private Stack[] deck;
    private int shownInDiscard = 3;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.board);
        setUp();
        bind();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUp() {
        deck = new Stack[2];
        deck[0] = new Stack(this, 1);
        deck[0].dealFullDeck();
        Collections.shuffle(deck[0]);
        deck[1] = new Stack(this, 3);

        // Setup Tableau
        tableau = new Stack[7];
        for (int row = 0; row < tableau.length; row++) {
            tableau[row] = new Stack(this, -1);
            System.out.println("Stack Created");
            for (int card = 0; card <= row; card++)
                tableau[row].addAll(deck[0].take());
            System.out.println("Deck Taken");
            tableau[row].get(tableau[row].size()-1).toggleShow();
        }

        // Setup Foundations
        foundations = new Stack[4];
        for (int row = 0; row < foundations.length; row++) {
            System.out.println("Foundation Created");
            foundations[row] = new Stack(this, 1);
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
        for (Stack s: foundations) {
            if (s.get(s.size() - 1).getNum() != 13)
                return false;
        }
        return true;
    }

    public boolean checkAddFoundation(Card c, int row) {
        Card last = foundations[row].get(foundations[row].size() - 1);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void flipToDiscard() {
        if (deck[0].size() == 0) {
            // no animation for reset deck[0], automatic
            Collections.reverse(deck[1]);
            deck[0].addAll(deck[1]);
        }

        if (deck[0].size() > 2) {
            deck[1].addAll(deck[0].take(deck[0].size() - 3));
        } else  {
            deck[1].addAll(deck[0]);
        }

        // we add to the end of the deck[1] and remove from the beginning of the ones shown
        while (deck[1].cardsShown().size() > shownInDiscard) {
            deck[1].get(deck[1].indexOf(deck[1].cardsShown().get(0))).toggleShow();
        }
    }
}
