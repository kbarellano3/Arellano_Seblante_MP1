package org.quinemccluskey.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    // Validate if minterms and don't cares have no duplicates
    public static boolean checkInputs(int[] minterms, int[] dontCares) {
        List<Integer> temp = new ArrayList<>();
        for (int minterm : minterms) {
            temp.add(minterm);
        }
        for (int dontCare : dontCares) {
            if (temp.contains(dontCare)) {
                return false;
            }
        }
        return true;
    }
    // Combine minterms and don't cares into a single binary string
    public static String mixTerms(String minterm1, String minterm2) {
        StringBuilder mixed = new StringBuilder();
        boolean[] added = new boolean[256];

        for (char c : minterm1.toCharArray()) {
            char upperCaseC = Character.toUpperCase(c);
            if (c != '-' && !added[upperCaseC]) {
                mixed.append(c);
                added[upperCaseC] = true;
            }
        }

        for (char c : minterm2.toCharArray()) {
            char upperCaseC = Character.toUpperCase(c);
            if (c != '-' && !added[upperCaseC]) {
                mixed.append(c);
                added[upperCaseC] = true;
            }
        }
        return mixed.toString();
    }

    // Check if two minterms are a valid combination
    public static boolean isValidCombination(Minterm minterm1, Minterm minterm2) {
        if (minterm1.getString().length() != minterm2.getString().length())
            return false;

        int k = 0;
        for (int i = 0; i < minterm1.getString().length(); i++) {
            if (minterm1.getString().charAt(i) == '-' && minterm2.getString().charAt(i) != '-')
                return false;
            else if (minterm1.getString().charAt(i) != '-' && minterm2.getString().charAt(i) == '-')
                return false;
            else if (minterm1.getString().charAt(i) != minterm2.getString().charAt(i))
                k++;
        }
        return k == 1;
    }
}
