package com.goramie.soliterrible;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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
        deck[0] = new Stack(this, 1, Stack.Type.DRAW, this);
        deck[0].dealFullDeck();
        Collections.shuffle(deck[0]);
        deck[1] = new Stack(this, 3, Stack.Type.DISCARD, this);

        // Setup Tableau
        tableau = new Stack[7];
        for (int row = 0; row < tableau.length; row++) {
            tableau[row] = new Stack(this, -1, Stack.Type.TABLEAU, this);
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
            foundations[row] = new Stack(this, 1, Stack.Type.FOUNDATION, this);
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

    public void flipToDiscard() {
        if (deck[0].size() == 0) {
            // no animation for reset deck[0], automatic
            System.out.println("reset");
            Collections.reverse(deck[1]);
            deck[0].addAll(deck[1].take(0));
            for (Card c: deck[0])
                c.toggleShow();
        } else {
            deck[1].addAll(deck[0].take());
            deck[1].get(deck[1].size() - 1).toggleShow();
        }
    }

    public void restartGame(View v) {
        startActivity(new Intent(this, MainMenu.class));
    }
}
