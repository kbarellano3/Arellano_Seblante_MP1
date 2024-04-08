package org.quinemccluskey.algorithm;

import java.util.ArrayList;
import java.util.List;

// Represents a minterm in the boolean function
public class Minterm {
    private final String minterm;
    private int onesCount;
    private final List<Integer> nums;

    // Constructor for initializing minterm with value and length
    public Minterm(int value, int length) {
        String binary = Integer.toBinaryString(value);
        StringBuilder temp = new StringBuilder(binary);
        while (temp.length() != length) {
            temp.insert(0, 0);
        }
        this.minterm = temp.toString();
        nums = new ArrayList<>();
        nums.add(value);

        // Calculate the count of ones in the term
        onesCount = 0;
        for (int i = 0; i < minterm.length(); i++) {
            if (minterm.charAt(i) == '1') {
                onesCount++;
            }
        }
    }

    // Constructor for combining two terms
    public Minterm(Minterm minterm1, Minterm minterm2) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < minterm1.getString().length(); i++) {
            if (minterm1.getString().charAt(i) != minterm2.getString().charAt(i)) {
                temp.append("-");
            } else {
                temp.append(minterm1.getString().charAt(i));
            }
        }
        this.minterm = temp.toString();
        onesCount = 0;
        for (int i = 0; i < minterm.length(); i++) {
            if (this.minterm.charAt(i) == '1') {
                onesCount++;
            }
        }
        nums = new ArrayList<>();
        nums.addAll(minterm1.getNums());
        nums.addAll(minterm2.getNums());
    }

    String getString() {
        return minterm;
    }

    List<Integer> getNums() {
        return nums;
    }

    int getOnesCount() {
        return onesCount;
    }
}
