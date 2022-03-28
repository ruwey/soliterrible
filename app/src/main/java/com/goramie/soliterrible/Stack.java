package com.goramie.soliterrible;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

public class Stack extends ArrayList<Card> {
    private Context c;
    //private LinearLayout l;
    private StackLayout l;

    public Stack(Context c, int s) {
        super();

        this.c = c;
        l = new StackLayout(this.c, s);
        //l.setOrientation(LinearLayout.VERTICAL);
    }

    public void dealFullDeck() {
        for (int type : Card.TYPES.keySet())
            for (int i = 0; i < 14; i++)
                this.add(new Card(c, type, i));
    }

    public ArrayList<Card> take() {
        ArrayList<Card> single = new ArrayList<Card>();
        l.removeView(this.get(this.size() - 1));
        single.add(super.remove(super.size() - 1));
        return single;
    }

    public ArrayList<Card> take(int idx) {
        ArrayList<Card> moving = new ArrayList<Card>();
        for (int i = idx; i < super.size(); i++) {
            l.removeView(this.get(i));
            moving.add(super.remove(i));
        }
        return moving;
    }

    public Stack cardsShown() {
        Stack shown = new Stack(c, -1);
        for (Card c : this) {
            if (c.isShowing()) shown.add(c);
        }
        return shown;
    }

    @Override
    public boolean add(Card c) {
        l.addView(c);
        return super.add(c);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Card> c) {
        for (Card i: c)
            l.addView(i);
        return super.addAll(c);
    }

    public StackLayout getLayout() {
        return l;
    }

    // Handles Most Drawing Stuff
    public class StackLayout extends ViewGroup {
        final int shown;
        public StackLayout(Context context, int s) {
            super(context);
            shown = s;
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            final int count = getChildCount();
            int curWidth, curHeight, curLeft, curTop, maxHeight;

            //get the available size of child view
            final int childLeft = this.getPaddingLeft();
            final int childTop = this.getPaddingTop();
            final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
            final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
            final int childWidth = childRight - childLeft;
            final int childHeight = childBottom - childTop;

            maxHeight = 0;
            curLeft = childLeft;
            curTop = childTop;

            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);

                if (child.getVisibility() == GONE)
                    return;

                //Get the maximum size of the child
                child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
                curWidth = child.getMeasuredWidth();
                curHeight = child.getMeasuredHeight();
                //wrap is reach to the end
                if (curLeft + curWidth >= childRight) {
                    curLeft = childLeft;
                    curTop += maxHeight;
                    maxHeight = 0;
                }
                //do the layout
                child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
                curTop += curHeight * 0.3;
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int count = getChildCount();
            // Measurement will ultimately be computing these values.
            int height = 0;
            int width = 0;
            int childState = 0;


            // Iterate through all children, measuring them and computing our dimensions
            // from their size.
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);

                if (child.getVisibility() == GONE)
                    continue;

                // Measure the child.
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                width = child.getMeasuredWidth();
                System.out.println("CW: " + width);

                height += child.getMeasuredHeight() * 0.3;
                if (i == 0)
                    height += child.getMeasuredHeight()*0.7;
                System.out.println("CH: " + height);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }

            // Check against our minimum height and width
//            maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());

            // Report our final dimensions.
            setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState),
                    resolveSizeAndState(height, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
        }
    }
}
