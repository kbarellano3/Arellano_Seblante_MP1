package org.quinemccluskey.algorithm;

import java.util.*;

/**
 * A class implementing the Quine-McCluskey algorithm for simplifying boolean expressions.
 *
 * <p>
 *     This class is responsible for creating the main logic of the QMA, which is referenced by the Main Controller.
 * </p>
 */
public class Quine_McCluskey {

    private final Minterm[] minterms;
    private final ArrayList<Integer> mintermsList;
    private ArrayList<Minterm> finalMinterms;
    private final ArrayList<Integer> dontCaresList;
    private ArrayList<String>[] solution;
    private final ArrayList<String> primeImplicants;
    public final String startVar;

    /**
     * Constructs a Quine_McCluskey object with minterms, don't cares, and a start variable.
     *
     * @param mintermsString String of minterms separated by commas or spaces
     * @param dontCaresString String of don't cares separated by commas or spaces
     * @param startVar String that starts the letters to be used for the simplified boolean expression
     */
    public Quine_McCluskey(String mintermsString, String dontCaresString, String startVar) {
        this.startVar = startVar;
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

    /**
     * Validates and parses the input string into an array of integers.
     *
     * <p>
     *     This method validates by throwing a RuntimeException for non-integer values or duplicate entries. If any
     *     such exception happens, an "Invalid input" message is outputted.
     * </p>
     *
     * @param input String to be validated and parsed
     * @return Array of integers parsed from the input string
     * @throws RuntimeException Exception if the input string is invalid
     */
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

    /**
     * Groups minterms based on the number of their ones.
     *
     * <p>
     *     This method adds minterms into a list of groups based on the called getOnesCount() method and returns the
     *     respective groups.
     * </p>
     *
     * @param termsArray Array of all minterms to be grouped
     * @return ArrayList of grouped minterms
     */
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

    /**
     * Performs the first step of the QMA.
     *
     * <p>
     *     This method determines the prime implicants from the list of binary string minterms. it implements the
     *     groupTerms() method, which is put into an indefinite loop until no minterms and groups can be grouped
     *     further. It then calls the solve2() method to çontinue the solving process.
     * </p>
     */
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

    /**
     * Performs the second step of the QMA.
     *
     * <p>
     *     This method determines the product of sums from the necessary prime implicants. It calls petricksMethod(),
     *     which is the method used for finding the essential and additional prime implicants to be used.
     * </p>
     */
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

    /**
     * Converts a minterm or group of minterms to its symbolic expression.
     *
     * <p>
     *     This method converts the binary representation of a term, with ones being a variable based on bit position,
     *     zeros being a primed variable, and "-" being a variable not included, into an expression with English
     *     letters. This method is also responsible for initializing the default start variable "A" or the custom start
     *     variable set by the user.
     * </p>
     *
     * @param term String minterm or group of minterms to be converted
     * @return String of the symbolic expression of a minterm or group of minterms
     */
    String toSymbolicExpression(String term) {
        char start = startVar.isEmpty() ? 'A' : startVar.charAt(0);

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


    /**
     * Petrick's method: used to find essential and additional prime implicants.
     *
     * <p>
     *     This method involves multiplying terms and performing manipulations to identify the necessary prime
     *     implicants for the boolean expression. This is a method done after the QMA.
     * </p>
     */
    void petricksMethod() {

        char start = startVar.isEmpty() ? 'A' : startVar.charAt(0);

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

    /**
     * Multiplies terms according to Petrick's method.
     *
     * <p>
     *     This method performs the multiplication of terms provided by the list of the necessary prime implicants.
     *     It recursively multiples terms from the array and add them to the result list. This results in a a minimal
     *     sum-of-products (SOP) expression.
     * </p>
     *
     * @param p Array of lists of terms to be multiplied
     * @param k Integer index indicating the current list being processed
     * @return String list of combined terms representing the minimal SOP expression
     */
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

    /**
     * Checks for column dominance among the prime implicants.
     *
     * <p>
     *     This method returns true once it has determined that all prime implicants that cover certain minterms do
     *     not share other minterms, indicating that the covered implicants are redundant and can be removed.
     * </p>
     *
     * @return Boolean determining if column dominance simplification was performed
     */
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

    /**
     * Checks for row dominance among the prime implicants.
     *
     * <p>
     *     This method returns true once it has determined that all the prime implicants can be covered by another
     *     prime implicant. This reduces redundancy of prime implicants.
     * </p>
     *
     * @return Boolean determining if row dominance simplification was performed
     */
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

    /**
     * Identifies from a list of minterms or group of minterms if they are prime implicants.
     *
     * <p>
     *     This method analyzes the list of minterms or group of minterms iteratively. It checks if the prime implicant
     *     covers a minterm that isn't covered by any other prime implicant in the list.
     * </p>
     *
     * @return Boolean determining if the minterms or group of minterms are prime implicants
     */
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

    /**
     * Checks if one minterm contains another.
     *
     * <p>
     *     This method checks if term1 contains all the integers that are covered by term2.
     * </p>
     *
     * @param term1 Minterm object of the first minterm or group of minterms
     * @param term2 Minterm object of the second minterm or group of minterms
     * @return Boolean determining if term1 contains term2
     */
    boolean contains(Minterm term1, Minterm term2) {
        if (term1.getNums().size() <= term2.getNums().size()) {
            return false;
        }
        List<Integer> a = term1.getNums();
        List<Integer> b = term2.getNums();
        b.removeAll(dontCaresList);

        return new HashSet<>(a).containsAll(b);
    }

    /**
     * Method that formats the solution list into a string.
     *
     * <p>
     *     This method appends all elements of the solution list into a Stringbuilder, which returns a string.
     * </p>
     *
     * @return String result of the algorithm
     */
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