package org.quinemccluskey.algorithm;

import java.util.*;

// Hello! Please feel free to change variable names! :)
// Comments are placeholders only for your own comments.
// Note: This code does not implement bonus feature yet.
// NOte from Sheianne: error handling when list of don't cares contains some minterms

public class Quine_McCluskey {

    private final Minterm[] minterms;
    private final ArrayList<Integer> mintermsList;
    private ArrayList<Minterm> finalMinterms;
    private final ArrayList<Integer> dontCaresList;
    private ArrayList<String>[] solution;
    private final ArrayList<String> primeImplicants;
    public final String startChar;

    // Constructor for Quine_McCluskey algorithm
    public Quine_McCluskey(String mintermsString, String dontCaresString, String startChar) {
        this.startChar = startChar;
        // Validate and initialize minterms and don't cares
        int[] minterms = isValid(mintermsString);
        int[] dontCares = isValid(dontCaresString);
        if (!Helper.checkInputs(minterms, dontCares)) {
            throw new RuntimeException("Invalid input");
        }

        Arrays.sort(dontCares);
        Arrays.sort(minterms);

        // Determine max binary length
        var maxBinaryLength = Integer.toBinaryString(minterms[minterms.length - 1]).length();

        // Initialize lists for minterms, don't cares, and prime implicants
        this.dontCaresList = new ArrayList<>();
        this.mintermsList = new ArrayList<>();
        primeImplicants = new ArrayList<>();

        // Initialize minterms array and populate it with minterms and don't cares
        Minterm[] temp = new Minterm[minterms.length + dontCares.length];
        int index = 0;
        for (int minterm : minterms) {
            temp[index++] = new Minterm(minterm, maxBinaryLength);
            this.mintermsList.add(minterm);
        }
        for (int dontCare : dontCares) {
            if (Integer.toBinaryString(dontCare).length() > maxBinaryLength) {
                break;
            }
            temp[index++] = new Minterm(dontCare, maxBinaryLength);
            this.dontCaresList.add(dontCare);
        }
        this.minterms = new Minterm[index];
        System.arraycopy(temp, 0, this.minterms, 0, index);

        // Sort terms array based on ones count
        Arrays.sort(this.minterms, new OnesComparator());
    }

    // Validate and parse input string to array of integers
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

    // Group terms based on ones count
    private ArrayList<Minterm>[] groupTerms(Minterm[] termsArray) {
        ArrayList<Minterm>[] groups = new ArrayList[termsArray[termsArray.length - 1].getOnesCount() + 1];

        for (int i = 0; i < groups.length; i++) {
            groups[i] = new ArrayList<>();
        }
        for (Minterm minterm : termsArray) {
            int k = minterm.getOnesCount();
            groups[k].add(minterm);
        }
        return groups;
    }

    // Perform 1st step of QM: Determine prime implicants from minterms
    public void solve1() {
        ArrayList<Minterm> remainingMinterms = new ArrayList<>();
        ArrayList<Minterm>[] groupedTerms = groupTerms(minterms);
        ArrayList<Minterm>[] results;

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
                        if (Helper.isValidCombination(groupedTerms[i].get(j), groupedTerms[i + 1].get(k))) {

                            combinedTerms.add(groupedTerms[i].get(j).getString());
                            combinedTerms.add(groupedTerms[i + 1].get(k).getString());

                            Minterm newMinterm = new Minterm(groupedTerms[i].get(j), groupedTerms[i + 1].get(k));
                            if (!temp.contains(newMinterm.getString())) {
                                results[i].add(newMinterm);
                                inserted = true;
                            }
                            temp.add(newMinterm.getString());
                        }
                    }
                }
            }

            if (inserted) {
                for (ArrayList<Minterm> mintermArrayList : groupedTerms) {
                    for (Minterm minterm : mintermArrayList) {
                        if (!combinedTerms.contains(minterm.getString())) {
                            remainingMinterms.add(minterm);
                        }
                    }
                }
                groupedTerms = results;
            }
        } while (inserted && groupedTerms.length > 1);

        finalMinterms = new ArrayList<>();
        for (ArrayList<Minterm> mintermArrayList : groupedTerms) {
            finalMinterms.addAll(mintermArrayList);
        }
        finalMinterms.addAll(remainingMinterms);
        solve2();
    }

    // Perform 2nd step of QM: Determine product of sums from necessary PIs
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

    String toSymbolicExpression(String term) {
        char start = startChar.isEmpty() ? 'A' : startChar.charAt(0);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < term.length(); i++) {
            char c = term.charAt(i);
            if (c == '-') {
                continue;
            } else if (c == '1' || c == '0') {
                char var;
                if (Character.isUpperCase(start)) {
                    var = (char) ((start - 'A' + i) % 26 + 'A');
                } else {
                    var = (char) ((start - 'a' + i) % 26 + 'a');
                }
                result.append(var);
                if (c == '0') {
                    result.append('\'');
                }
            }
        }
        if (result.isEmpty()) {
            result.append("1");
        }
        return result.toString();
    }


    // Petrick's method; Used to find essential and additional PIs
    void petricksMethod() {

        char start = startChar.isEmpty() ? 'A' : startChar.charAt(0);

        List<List<String>> temp = new ArrayList<>();
        for (int i = 0; i < mintermsList.size(); i++) {
            temp.add(new ArrayList<>());
            for (int j = 0; j < finalMinterms.size(); j++) {
                if (finalMinterms.get(j).getNums().contains(mintermsList.get(i))) {
                    char t = (char) (start + j);
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
                    solution[k].add(finalMinterms.get((int) c.charAt(i) - start).getString());
                }
                for (String primeTerm : primeImplicants) {
                    solution[k].add(primeTerm);
                }
                k++;
            }
        }
    }

    // Multiply terms according to Petrick's method
    List<String> multiply(ArrayList<String>[] p, int k) {
        if (k >= p.length - 1) {
            return p[k];
        }

        ArrayList<String> resultList = new ArrayList<>();
        for (String firstListTerm : p[k]) {
            for (String secondListTerm : p[k + 1]) {
                String mixed = Helper.mixTerms(firstListTerm, secondListTerm);
                if (!resultList.contains(mixed)) {
                    resultList.add(mixed);
                }
            }
        }
        p[k + 1] = resultList;

        return multiply(p, k + 1);
    }

    // Perform column dominance checking
    private boolean columnDominance() {
        boolean flag = false;
        List<List<Integer>> cols = new ArrayList<>();
        for (int i = 0; i < mintermsList.size(); i++) {
            cols.add(new ArrayList<>());
            for (int j = 0; j < finalMinterms.size(); j++) {
                if (finalMinterms.get(j).getNums().contains(mintermsList.get(i))) {
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

    // Perform row dominance checking
    private boolean rowDominance() {
        boolean flag = false;
        for (int i = 0; i < finalMinterms.size() - 1; i++) {
            for (int j = i + 1; j < finalMinterms.size(); j++) {
                if (contains(finalMinterms.get(i), finalMinterms.get(j))) {
                    finalMinterms.remove(j);
                    j--;
                    flag = true;
                } else if (contains(finalMinterms.get(j), finalMinterms.get(i))) {
                    finalMinterms.remove(i);
                    i--;
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    // Identify prime implicants
    private boolean identifyPrimeImplicants() {
        List<List<Integer>> cols = new ArrayList<>();
        for (int i = 0; i < mintermsList.size(); i++) {
            cols.add(new ArrayList<>());
            for (int j = 0; j < finalMinterms.size(); j++) {
                if (finalMinterms.get(j).getNums().contains(mintermsList.get(i))) {
                    cols.get(i).add(j);
                }
            }
        }
        boolean flag = false;
        for (int i = 0; i < mintermsList.size(); i++) {
            if (cols.get(i).size() == 1) {
                flag = true;
                List<Integer> del = finalMinterms.get(cols.get(i).get(0)).getNums();

                for (int j = 0; j < mintermsList.size(); j++) {
                    if (del.contains(mintermsList.get(j))) {
                        dontCaresList.add(mintermsList.get(j));
                        mintermsList.remove(j);
                        j--;
                    }
                }
                primeImplicants.add(finalMinterms.get(cols.get(i).get(0)).getString());
                finalMinterms.remove(cols.get(i).get(0).intValue());
                break;
            }
        }
        return flag;
    }

    // Check if one minterm contains another
    boolean contains(Minterm term1, Minterm term2) {
        if (term1.getNums().size() <= term2.getNums().size()) {
            return false;
        }
        List<Integer> a = term1.getNums();
        List<Integer> b = term2.getNums();
        b.removeAll(dontCaresList);

        return new HashSet<>(a).containsAll(b);
    }

    // Method to get results as a string
    public String getResultsAsString() {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < solution.length; i++) {
            resultBuilder.append("Solution ").append(i + 1).append(":\n");
            for (int j = 0; j < solution[i].size(); j++) {
                resultBuilder.append(toSymbolicExpression(solution[i].get(j)));
                if (j != solution[i].size() - 1) {
                    resultBuilder.append(" + ");
                }
            }
            resultBuilder.append("\n\n");
        }
        return resultBuilder.toString();
    }
}

//    public static void main(String[] args) {
//        // Loop program until user exits
//        while (true) {
//            // Prompt user for minterms and don't cares
//            Scanner userInput = new Scanner(System.in);
//            System.out.print("Minterms (separated by space or comma): ");
//            String mintermsInput = userInput.nextLine();
//
//            String dontCaresInput = "";
//            String continueProgram = "";
//
//            // Check for invalid input
//            while (true) {
//                System.out.print("Do you want to include don't cares? (yes/no): ");
//                String includeDontCaresInput = userInput.nextLine().toLowerCase();
//
//                // Ask user if they want to include don't cares
//                if (includeDontCaresInput.equals("yes")) {
//                    System.out.print("Don't Cares (separated by space or comma): ");
//                    dontCaresInput = userInput.nextLine();
//                    break;
//                }
//                else if (includeDontCaresInput.equals("no")) {
//                    break;
//                }
//                else {
//                    System.out.println("Invalid input. Put 'yes' or 'no'.");
//                }
//            }
//            // Initialize Quine_McCluskey object with MTs and DCs
//            try {
//                Quine_McCluskey qm = new Quine_McCluskey(mintermsInput, dontCaresInput);
//
//                System.out.println();
//                // Solve and print results
//                qm.solve1();
//                qm.printResults();
//            } catch (RuntimeException e) {
//                System.out.println("\nRUNTIME EXCEPTION: Make sure your don't cares are correct\n");
//            }
//
//            System.out.print("Would you like to continue? (yes/no): ");
//            continueProgram = userInput.nextLine().toLowerCase();
//
//            if (continueProgram.equals("yes")) {
//                System.out.println();
//                continue;
//            }
//            else if (continueProgram.equals("no")) {
//                break;
//            }
//            else {
//                System.out.println("Invalid input. Put 'yes' or 'no'");
//                break;
//            }
//        }
//        System.out.println("\nThank you for using our program!");
//    }

//    // Print the final result/s
//    public void printResults() {
//        for (int i = 0; i < solution.length; i++) {
//            System.out.println("Solution " + (i + 1) + ":");
//            for (int j = 0; j < solution[i].size(); j++) {
//                System.out.print(toSymbolicExpression(solution[i].get(j)));
//                if (j != solution[i].size() - 1) {
//                    System.out.print(" + ");
//                }
//            }
//            System.out.println("\n");
//        }
//    }