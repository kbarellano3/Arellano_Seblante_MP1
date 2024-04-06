package org.example;

import java.util.*;

public class Quine_McCluskey {

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Minterms (separated by space or comma): ");
        String mintermsInput = userInput.nextLine();

        System.out.print("Do you want to include don't cares? (yes/no): ");
        String includeDontCaresInput = userInput.nextLine().toLowerCase();
        String dontCaresInput = "";

        if (includeDontCaresInput.equals("yes")) {
            System.out.print("Don't Cares (separated by space or comma): ");
            dontCaresInput = userInput.nextLine();
        }

        Quine_McCluskey qm = new Quine_McCluskey(mintermsInput, dontCaresInput);

        System.out.println();
        qm.solve1();
        qm.printResults();
    }


    private class Term {
        private final String term;
        private int onesCount;
        private final List<Integer> nums;

        public Term(int value, int length) {
            String binary = Integer.toBinaryString(value);
            StringBuilder temp = new StringBuilder(binary);
            while (temp.length() != length) {
                temp.insert(0, 0);
            }
            this.term = temp.toString();
            nums = new ArrayList<>();
            nums.add(value);
            onesCount = 0;
            for (int i = 0; i < term.length(); i++) {
                if (term.charAt(i) == '1') {
                    onesCount++;
                }
            }
        }

        public Term(Term term1, Term term2) {
            StringBuilder temp = new StringBuilder();
            for (int i = 0; i < term1.getString().length(); i++) {
                if (term1.getString().charAt(i) != term2.getString().charAt(i)) {
                    temp.append("-");
                } else {
                    temp.append(term1.getString().charAt(i));
                }
            }
            this.term = temp.toString();
            onesCount = 0;
            for (int i = 0; i < term.length(); i++) {
                if (this.term.charAt(i) == '1') {
                    onesCount++;
                }
            }
            nums = new ArrayList<>();
            nums.addAll(term1.getNums());
            nums.addAll(term2.getNums());
        }

        String getString() {
            return term;
        }

        List<Integer> getNums() {
            return nums;
        }

        int getOnesCount() {
            return onesCount;
        }
    }


    public static class OnesComparator implements Comparator<Term> {
        @Override
        public int compare(Term t1, Term t2) {
            return t1.getOnesCount() - t2.getOnesCount();
        }
    }

    private final Term[] terms;
    private final ArrayList<Integer> mintermsList;
    private final ArrayList<Integer> dontCaresList;
    private ArrayList<String>[] solution;
    private final ArrayList<String> primeImplicants;
    private ArrayList<Term> finalTerms;
    private final int maxBinaryLength;

    public Quine_McCluskey(String mintermsString, String dontCaresString) {
        int[] minterms = isValid(mintermsString);
        int[] dontCares = isValid(dontCaresString);
        if (!checkInputs(minterms, dontCares)) {
            throw new RuntimeException("Invalid input");
        }

        Arrays.sort(dontCares);
        Arrays.sort(minterms);

        maxBinaryLength = Integer.toBinaryString(minterms[minterms.length - 1]).length();

        this.dontCaresList = new ArrayList<>();
        this.mintermsList = new ArrayList<>();
        primeImplicants = new ArrayList<>();

        Term[] temp = new Term[minterms.length + dontCares.length];
        int index = 0;
        for (int minterm : minterms) {
            temp[index++] = new Term(minterm, maxBinaryLength);
            this.mintermsList.add(minterm);
        }
        for (int dontCare : dontCares) {
            if (Integer.toBinaryString(dontCare).length() > maxBinaryLength) {
                break;
            }
            temp[index++] = new Term(dontCare, maxBinaryLength);
            this.dontCaresList.add(dontCare);
        }
        terms = new Term[index];
        System.arraycopy(temp, 0, terms, 0, index);

        Arrays.sort(terms, new OnesComparator());
    }

    boolean checkInputs(int[] minterms, int[] dontCares) {
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

    private int[] isValid(String input) {
        input = input.replace(",", " ");
        if (input.trim().isEmpty()) {
            return new int[] {};
        }
        String[] array = input.trim().split(" +");
        int[] temp = new int[array.length];
        for (int i = 0; i < temp.length; i++) {
            try {
                int value = Integer.parseInt(array[i]);
                temp[i] = value;
            } catch (Exception e) {
                throw new RuntimeException("Invalid input");
            }
        }
        List<Integer> duplicates = new ArrayList<>();
        for (int j : temp) {
            if (duplicates.contains(j)) {
                throw new RuntimeException("Invalid input");
            }
            duplicates.add(j);
        }
        return temp;
    }

    private ArrayList<Term>[] groupTerms(Term[] termsArray) {
        ArrayList<Term>[] groups = new ArrayList[termsArray[termsArray.length - 1].getOnesCount() + 1];

        for (int i = 0; i < groups.length; i++) {
            groups[i] = new ArrayList<>();
        }
        for (Term term : termsArray) {
            int k = term.getOnesCount();
            groups[k].add(term);
        }
        return groups;
    }

    public void solve1() {
        ArrayList<Term> remainingTerms = new ArrayList<>();
        ArrayList<Term>[] groupedTerms = groupTerms(terms);
        ArrayList<Term>[] results;

        boolean inserted;
        do {
            List<String> combinedTerms = new ArrayList<>();
            results = new ArrayList[groupedTerms.length - 1];

            List<String> temp;
            inserted = false;

            for (int i = 0; i < groupedTerms.length - 1; i++) {
                results[i] = new ArrayList<>();
                temp = new ArrayList<>();

                for (int j = 0; j < groupedTerms[i].size(); j++) {
                    for (int k = 0; k < groupedTerms[i + 1].size(); k++) {
                        if (isValidCombination(groupedTerms[i].get(j), groupedTerms[i + 1].get(k))) {

                            combinedTerms.add(groupedTerms[i].get(j).getString());
                            combinedTerms.add(groupedTerms[i + 1].get(k).getString());

                            Term newTerm = new Term(groupedTerms[i].get(j), groupedTerms[i + 1].get(k));
                            if (!temp.contains(newTerm.getString())) {
                                results[i].add(newTerm);
                                inserted = true;
                            }
                            temp.add(newTerm.getString());
                        }
                    }
                }
            }

            if (inserted) {
                for (ArrayList<Term> termArrayList : groupedTerms) {
                    for (Term term : termArrayList) {
                        if (!combinedTerms.contains(term.getString())) {
                            remainingTerms.add(term);
                        }
                    }
                }
                groupedTerms = results;
            }
        } while (inserted && groupedTerms.length > 1);

        finalTerms = new ArrayList<>();
        for (ArrayList<Term> termArrayList : groupedTerms) {
            finalTerms.addAll(termArrayList);
        }
        finalTerms.addAll(remainingTerms);
        solve2();
    }

    public void solve2() {
        if (!identifyPrimeImplicants()) {
            if (!rowDominance()) {
                if (!columnDominance()) {
                    petricksMethod();
                    return;
                }
            }
        }
        if (!mintermsList.isEmpty())
            solve2();
        else {
            solution = new ArrayList[1];
            solution[0] = primeImplicants;
        }
    }

    void petricksMethod() {
        List<List<String>> temp = new ArrayList<>();
        for (int i = 0; i < mintermsList.size(); i++) {
            temp.add(new ArrayList<>());
            for (int j = 0; j < finalTerms.size(); j++) {
                if (finalTerms.get(j).getNums().contains(mintermsList.get(i))) {
                    char t = (char) ('a' + j);
                    temp.get(i).add(t + "");
                }
            }
        }

        List<String> finalResult = multiply(temp.toArray(new ArrayList[0]), 0);

        List<String>[] step = new List[1];
        step[0] = finalResult;

        int min = -1;
        int count = 0;
        for (String m : finalResult) {
            if (min == -1 || m.length() < min) {
                min = m.length();
                count = 1;
            } else if (min == m.length()) {
                count++;
            }
        }

        solution = new ArrayList[count];
        int k = 0;
        for (String c : finalResult) {
            if (c.length() == min) {
                solution[k] = new ArrayList<>();
                for (int i = 0; i < c.length(); i++) {
                    solution[k].add(finalTerms.get((int) c.charAt(i) - 'a').getString());
                }
                for (String primeTerm : primeImplicants) {
                    solution[k].add(primeTerm);
                }
                k++;
            }
        }
    }

    String mixTerms(String term1, String term2) {
        StringBuilder mixed = new StringBuilder();
        boolean[] added = new boolean[256];

        for (char c : term1.toCharArray()) {
            char upperCaseC = Character.toUpperCase(c);
            if (c != '-' && !added[upperCaseC]) {
                mixed.append(c);
                added[upperCaseC] = true;
            }
        }

        for (char c : term2.toCharArray()) {
            char upperCaseC = Character.toUpperCase(c);
            if (c != '-' && !added[upperCaseC]) {
                mixed.append(c);
                added[upperCaseC] = true;
            }
        }
        return mixed.toString();
    }

    List<String> multiply(ArrayList<String>[] p, int k) {
        if (k >= p.length - 1) {
            return p[k];
        }

        ArrayList<String> resultList = new ArrayList<>();
        for (String firstListTerm : p[k]) {
            for (String secondListTerm : p[k + 1]) {
                String mixed = mixTerms(firstListTerm, secondListTerm);
                if (!resultList.contains(mixed)) {
                    resultList.add(mixed);
                }
            }
        }
        p[k + 1] = resultList;

        return multiply(p, k + 1);
    }

    private boolean columnDominance() {
        boolean flag = false;
        List<List<Integer>> cols = new ArrayList<>();
        for (int i = 0; i < mintermsList.size(); i++) {
            cols.add(new ArrayList<>());
            for (int j = 0; j < finalTerms.size(); j++) {
                if (finalTerms.get(j).getNums().contains(mintermsList.get(i))) {
                    cols.get(i).add(j);
                }
            }
        }
        for (int i = 0; i < cols.size() - 1; i++) {
            for (int j = i + 1; j < cols.size(); j++) {
                if (new HashSet<>(cols.get(j)).containsAll(cols.get(i)) && cols.get(j).size() > cols.get(i).size()) {
                    cols.remove(j);
                    mintermsList.remove(j);
                    j--;
                    flag = true;
                } else if (new HashSet<>(cols.get(i)).containsAll(cols.get(j)) && cols.get(i).size() > cols.get(j).size()) {
                    cols.remove(i);
                    mintermsList.remove(i);
                    i--;
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    private boolean rowDominance() {
        boolean flag = false;
        for (int i = 0; i < finalTerms.size() - 1; i++) {
            for (int j = i + 1; j < finalTerms.size(); j++) {
                if (contains(finalTerms.get(i), finalTerms.get(j))) {
                    finalTerms.remove(j);
                    j--;
                    flag = true;
                } else if (contains(finalTerms.get(j), finalTerms.get(i))) {
                    finalTerms.remove(i);
                    i--;
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    private boolean identifyPrimeImplicants() {
        List<List<Integer>> cols = new ArrayList<>();
        for (int i = 0; i < mintermsList.size(); i++) {
            cols.add(new ArrayList<>());
            for (int j = 0; j < finalTerms.size(); j++) {
                if (finalTerms.get(j).getNums().contains(mintermsList.get(i))) {
                    cols.get(i).add(j);
                }
            }
        }
        boolean flag = false;
        for (int i = 0; i < mintermsList.size(); i++) {
            if (cols.get(i).size() == 1) {
                flag = true;
                List<Integer> del = finalTerms.get(cols.get(i).get(0)).getNums();

                for (int j = 0; j < mintermsList.size(); j++) {
                    if (del.contains(mintermsList.get(j))) {
                        dontCaresList.add(mintermsList.get(j));
                        mintermsList.remove(j);
                        j--;
                    }
                }
                primeImplicants.add(finalTerms.get(cols.get(i).get(0)).getString());
                finalTerms.remove(cols.get(i).get(0).intValue());
                break;
            }
        }
        return flag;
    }

    boolean isValidCombination(Term term1, Term term2) {
        if (term1.getString().length() != term2.getString().length())
            return false;

        int k = 0;
        for (int i = 0; i < term1.getString().length(); i++) {
            if (term1.getString().charAt(i) == '-' && term2.getString().charAt(i) != '-')
                return false;
            else if (term1.getString().charAt(i) != '-' && term2.getString().charAt(i) == '-')
                return false;
            else if (term1.getString().charAt(i) != term2.getString().charAt(i))
                k++;
        }
        return k == 1;
    }

    boolean contains(Term term1, Term term2) {
        if (term1.getNums().size() <= term2.getNums().size()) {
            return false;
        }
        List<Integer> a = term1.getNums();
        List<Integer> b = term2.getNums();
        b.removeAll(dontCaresList);

        return new HashSet<>(a).containsAll(b);
    }

    String toSymbolicExpression(String term) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < term.length(); i++) {
            char c = term.charAt(i);
            if (c == '-') {
                continue;
            } else if (c == '1') {
                result.append((char) ('A' + i));
            } else if (c == '0') {
                result.append((char) ('A' + i)).append('\'');
            }
        }
        if (result.isEmpty()) {
            result.append("1");
        }
        return result.toString();
    }

    public void printResults() {
        for (int i = 0; i < solution.length; i++) {
            System.out.println("Solution #" + (i + 1) + ":");
            for (int j = 0; j < solution[i].size(); j++) {
                System.out.print(toSymbolicExpression(solution[i].get(j)));
                if (j != solution[i].size() - 1) {
                    System.out.print(" + ");
                }
            }
            System.out.println("\n");
        }
    }

}
