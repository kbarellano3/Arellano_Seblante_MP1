package org.quinemccluskey.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a minterm in the boolean function
 *
 * <p>
 *     This class is used to represent a minterm and all terms that follow after operations are done in the minterms.
 *     These terms are used in the QMA to be further simplified to boolean expressions.
 * </p>
 */
public class Minterm {
    private final String minterm;
    private int onesCount;
    private final List<Integer> nums;

    /**
     * Constructs the binary representation of a minterm.
     *
     * <p>
     *     This method/constructor is responsible for converting each minterm integer into a binary string. This would
     *     then be modified with leading zeros depending on the specified length of the maximum minterm. Lastly,
     *     the number of ones in the minterm is counted to group them properly.
     * </p>
     * @param value Integer value of the minterm being constructed.
     * @param length Integer length of the minterm that serves as the basis for leading zeros.
     */
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

    /**
     * Constructs the merged version of a minterm.
     *
     * <p>
     *     This method is responsible for properly merging and formatting two minterms that are compatible for grouping.
     *     The program does so by adding a "-" when two bits are different and updating their ones count.
     * </p>
     *
     * @param minterm1 First minterm object to be compared.
     * @param minterm2 Second minterm object to be compared.
     */
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

    /**
     * Getter for the binary string representation of a minterm.
     *
     * @return Returns the binary string representation of a minterm.
     */
    String getString() {
        return minterm;
    }

    /**
     * Getter for the integer values of the minterms and terms being used in the QMA.
     *
     * @return Returns the list of integer values of the minterms and terms being used.
     */
    List<Integer> getNums() {
        return nums;
    }

    /**
     * Getter for the number of ones in minterms and terms.
     *
     * @return Returns the number of ones in minterms and terms.
     */
    int getOnesCount() {
        return onesCount;
    }
}
