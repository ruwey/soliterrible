package com.goramie.soliterrible;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collection;

public class Stack extends ArrayList<Card> {
    private Context c;
    //private LinearLayout l;
    private ViewGroup l;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Stack(Context c, int s) {
        super();

        this.c = c;

        if (s == -1)
            l = new StackLayout(this.c);
        else
            l = new FrameLayout(this.c);
        l.setOnLongClickListener((View v) -> {
            Stack temp = new Stack(c, -1);
            int pos = Math.min(this.size()-1,
                    (int)(v.getY()/(l.getChildAt(0).getMeasuredHeight()*.3)));
            temp.addAll(this.take(pos));
            temp.getLayout().startDragAndDrop(null, new View.DragShadowBuilder(temp.getLayout()), temp, 0);
            return true;
        });

        l.setOnDragListener((view, dragEvent) -> {
            System.out.println("hello");

            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    final Stack moving = (Stack) dragEvent.getLocalState();
                    // THIS IS WHERE THE POSSIBLE MOVE LOGIC NEEDS TO GO. THE MOVING CARDS ARE
                    // REFERED TO AS moving
                    return true;

                case DragEvent.ACTION_DROP:
                    this.addAll((Stack) dragEvent.getLocalState());
                    return true;
            }

            return false;
        });
        //l.setOrientation(LinearLayout.VERTICAL);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void dealFullDeck() {
        for (int type : Card.TYPES.keySet())
            for (int i = 0; i < 14; i++)
                this.add(new Card(c, type, i));
    }

    public ArrayList<Card> take() {
        ArrayList<Card> single = new ArrayList<>();
        l.removeView(this.get(this.size() - 1));
        single.add(super.remove(super.size() - 1));
        return single;
    }

    public ArrayList<Card> take(int idx) {
        ArrayList<Card> moving = new ArrayList<>();
        for (int i = idx; i < super.size(); i++) {
            l.removeView(this.get(i));
            moving.add(super.remove(i));
        }
        return moving;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        c.setParent(this);
        return super.add(c);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Card> c) {
        for (Card i: c)
            l.addView(i);
        return super.addAll(c);
    }

    public ViewGroup getLayout() {
        return l;
    }

    // Handles Most Drawing Stuff
    public class StackLayout extends ViewGroup {
        public StackLayout(Context context) {
            super(context);
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
                    continue;

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
//                System.out.println("CW: " + width);

                height += child.getMeasuredHeight() * 0.3;
                if (i == 0)
                    height += child.getMeasuredHeight()*0.7;
//                System.out.println("CH: " + height);
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
