package org.quinemccluskey.algorithm;

import java.util.Comparator;

// Comparator for comparing minterms based on ones count
public class OnesComparator implements Comparator<Minterm> {
    @Override
    public int compare(Minterm t1, Minterm t2) {
        return t1.getOnesCount() - t2.getOnesCount();
    }
}
