package org.quinemccluskey.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that contains all the helper methods of the QMA.
 */
public class Helper {

    /**
     * Validates if minterms and don't cares have no duplicates.
     *
     * <p>
     *     This method checks whether the arrays of minterms and don't cares contain any duplicate values.
     * </p>
     *
     * @param minterms Integer array of minterms
     * @param dontCares Integer array of don't cares
     * @return Returns true if both arrays have no duplicates, false otherwise
     */
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

    /**
     * Combines minterms and don't cares into a single binary string.
     *
     * <p>
     *     This method combines two binary strings representing minterms or don't cares into a single binary string
     *     by appending characters from each string while avoiding duplicates.
     * </p>
     *
     * @param minterm1 String of the first binary minterm representing a minterm or don't care
     * @param minterm2 String of the second binary minterm representing a minterm or don't care
     * @return String combination of the two minterms with no duplicate characters
     */
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


    /**
     * Checks if two minterms form a valid combination.
     *
     * <p>
     *     This method verifies whether two minterms can be combined into a new minterm by checking if they differ in
     *     only one bit position.
     * </p>
     *
     * @param minterm1 Minterm object of the first minterm
     * @param minterm2 Minterm object of the second minterm
     * @return Boolean determining if the minterms can be combined
     */
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
