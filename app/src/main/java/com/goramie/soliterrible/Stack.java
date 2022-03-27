package com.goramie.soliterrible;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

public class Stack extends ArrayList<Card> {
    private Context c;
    //private LinearLayout l;
    private StackLayout l;

    public Stack(Context c) {
        super();

        this.c = c;
        l = new StackLayout(this.c);
        //l.setOrientation(LinearLayout.VERTICAL);
    }

    public void dealFullDeck() {
        for (int type : Card.TYPES)
            for (int i = 0; i < 14; i++)
                this.add(new Card(c, type, i));
    }

    public ArrayList<Card> take() {
        ArrayList<Card> single = new ArrayList<Card>();
        l.removeView(this.get(this.size() - 1).getView());
        single.add(super.remove(super.size() - 1));
        return single;
    }

    public ArrayList<Card> take(int idx) {
        ArrayList<Card> moving = new ArrayList<Card>();
        for (int i = idx; i < super.size(); i++) {
            l.removeView(this.get(i).getView());
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
        for (Card i: c)
            l.addView(i.getView());
        return super.addAll(c);
    }

    public StackLayout getLayout() {
        return l;
    }

    // Handles Most Drawing Stuff
    public static class StackLayout extends ViewGroup {
        private final boolean isVertical;
        private final ImageView empty;

        public StackLayout(Context context) {
            super(context);

            isVertical = true;
            empty = new ImageView(context);
            empty.setImageResource(R.drawable.ic_card);
        }

        @Override
        public void addView(View child) {
            super.addView(child);
        }

        @Override
        protected void onDraw(Canvas c) {
            System.out.println("tgest");
            if (getChildCount() == 0) {
                System.out.println("Drawing empty cell");
                empty.getDrawable().draw(c);
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            final int children = getChildCount();

            int curPos = 0;

            int width = getMeasuredWidth();
            int height = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();

            System.out.println("width " + width + " height: " + height);

            for (int c = 0; c < children; c++) {
                final View child = getChildAt(c);
                child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                System.out.println("Asigning Child " + child);
                child.layout(l, t + curPos, l+ child.getWidth(), t + child.getHeight());
                curPos += child.getHeight() * 0.3;
            }
        }

        @Override
        protected void onMeasure(int widthSpec, int heightSpec) {
            final int children = getChildCount();

            int width = 0;
            int height = 0;
            int childState = 0;

            for (int c = 0; c < children; c++) {
                final View child = getChildAt(c);
                if (child.getVisibility() != GONE) {
                    measureChild(child, widthSpec, heightSpec );

                    width = Math.max(width, child.getMeasuredWidth());
                    height += child.getMeasuredHeight()*0.3;
                    childState = combineMeasuredStates(childState, child.getMeasuredState());
                }
            }
            if (getChildAt(1) != null)
                height += getChildAt(0).getMeasuredHeight() * 0.7;

            setMeasuredDimension(resolveSizeAndState(width, widthSpec, childState), resolveSizeAndState(height, heightSpec, childState));
        }
    }
}
