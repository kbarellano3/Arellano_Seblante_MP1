package org.quinemccluskey.algorithm;

import java.util.Comparator;

/**
 * A comparator class that compares minterms based on the count of ones.
 */
public class OnesComparator implements Comparator<Minterm> {
    /**
     * Sorts minterms in ascending order of ones count.
     *
     * @param t1 Minterm object t1 to be compared
     * @param t2 Minterm object t2 to be compared
     * @return Integer of a negative, zero, or positive value depending on the outcome of comparing t1 and t2
     */
    @Override
    public int compare(Minterm t1, Minterm t2) {
        return t1.getOnesCount() - t2.getOnesCount();
    }
}
