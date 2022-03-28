package com.goramie.soliterrible;

import androidx.annotation.NonNull;
import androidx.core.app.RemoteInput;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Basement {
    public static void main(String[] args){
        ArrayList<Integer> ints = new ArrayList<>(Arrays.asList(1, 0, -1));
        System.out.println("Starting: " + ints);
        System.out.println("Sorted with insertionSort: " + insertionSort(ints, (a, b) -> (int) a < (int) b));
        System.out.println("Sorted with selectionSort: " + selectionSort(ints, (a, b) -> (int) a < (int) b));

        ArrayList<String> strs = new ArrayList<>(Arrays.asList("zebra", "alpha", "aaaa"));
        System.out.println("\nStarting: " + strs);
        System.out.println("Sorted with insertionSort: " + insertionSort(strs, (a, b) -> ((String) a).compareTo((String) b) < 0));
        System.out.println("Sorted with selectionSort: " + selectionSort(strs, (a, b) -> ((String) a).compareTo((String) b) < 0));

        // Passing in arrays?
        String[] technicallyThisCounts = {"i", "mean", "it", "does", "sorta"}; // more or less
        System.out.println("\nStarting: " + Arrays.toString(technicallyThisCounts));
        System.out.println("Sorted with insertion sort: " + Arrays.toString(insertionSort(technicallyThisCounts,(a, b) -> ((String) a).compareTo((String) b) < 0)));
        System.out.println("Sorted with selection sort: " + Arrays.toString(selectionSort(technicallyThisCounts,(a, b) -> ((String) a).compareTo((String) b) < 0)));

        ArrayList<Step> basement_stairs = new ArrayList<>(Arrays.asList(new Stair(), new Stair(), new Stair()));
        Step[] more_stairs = {new Stair(), new Stair(), new Stair()};

        System.out.println(basement_stairs);
        System.out.println(Arrays.toString(more_stairs));
    }

    public static ArrayList insertionSort(ArrayList a, Check c) {
        a = new ArrayList(a); // avoid mutating arraylist
        for (int i = 1; i < a.size(); ++i) {
            int j = i - 1;
            while ((j >= 0) && c.run(a.get(i), a.get(j))) {
                a.add(j, a.remove(i));
                j--;
                i--;
            }
        }
        return a;
    }

    public static Object[] insertionSort(Object[] a, Check c) {
        return insertionSort(new ArrayList(Arrays.asList(a)), c).toArray();
    }

    public static ArrayList selectionSort(ArrayList a, Check c) {
        a = new ArrayList(a); // avoid mutating arraylist
        for (int i = 0; i < a.size() - 1; i++) {
            int yeah = i;
            for (int j = i + 1; j < a.size(); j++) {
                if (c.run(a.get(j), a.get(yeah)))
                    yeah = j;
            }
            Object temp = a.get(yeah);
            a.set(yeah, a.get(i));
            a.set(i, temp);
        }
        return a;
    }

    public static Object[] selectionSort(Object[] a, Check c) {
        return selectionSort((new ArrayList(Arrays.asList(a))), c).toArray();
    }
}

class Step {
    private final String down_ascii = "┓";
    private final String up_ascii = "┏";

    public String getDown_ascii() {
        return down_ascii;
    }

    public String getUp_ascii() {
        return up_ascii;
    }
}

class Stair extends Step {
    private int direction;

    public Stair() {
        this.direction = -1; // The default is down.
    }
    public Stair(int direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return direction <= 0 ? getDown_ascii() : getUp_ascii();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stair stair = (Stair) o;
        return direction == stair.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction);
    }
}

interface Check {
    boolean run(Object a, Object b);
}
