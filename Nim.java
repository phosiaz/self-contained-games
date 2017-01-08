/* Description: program reads in a file to set up a game of Nim, then allows the user to play
Nim against the computer. .txt file must be integer numbers indicating the amount of sticks in the
pile, and using a carriage return to separate piles.*/

import java.io.*;
import java.util.*;

public class Nim {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter name of input file - no extension please: ");
        String fileName = input.next();
        fileName = checkValid(input, fileName);
        File recordList = new File(fileName + ".txt");
        int[] pile = interpretFile(recordList);
        if (Arrays.equals(pile, null)) {
            System.out.println("Error: file only contains bad records!");
        } else {
            System.out.println("There are " + pile.length + " valid piles.");
            while (totalRemainingSticks(pile) > 0) {
                System.out.println("Nim Game - CS1101 Fall 2016:");
                dumpPile(pile);
                userPlay(pile, input);
                if (totalRemainingSticks(pile) == 0) {
                    System.out.println("Congratulations - you win.");
                } else {
                    computerPlay(pile);
                    if (totalRemainingSticks(pile) == 0) {
                        System.out.println("Sorry - computer win.");
                    }
                }
            }
        }
        System.out.print("\nEnd of program.");
    }

    /**
     * This method simulates the computer's turn, how many sticks the computer will remove from a
     * pile dependent on the rules of nim addition, and removes the sticks
     * @param pile an array filled with the number of piles and the number of sticks in each pile
     */
    public static void computerPlay(int[] pile) {
        int result = 0, nim;
        int numRealPiles = 0;
        int realPileNum = 0;
        for (int i = 0; i < pile.length; i++) {//makes sure more than one nonempty pile remains
            if (pile[i] != 0) {
                numRealPiles++;
                realPileNum = i;
            }
        }
        if (numRealPiles > 1) {
            result = nimAddition(pile[0], pile[1]);
            for (int i = 2; i < pile.length; i++) {
                result = nimAddition(result, pile[i]);
            }
            for (int i = 0; i < pile.length; i++) {
                nim = nimAddition(result, pile[i]);
                if (nim < pile[i]) {
                    System.out.println("Computer removes " + (pile[i] - nim)
                            + " from pile " + (i + 1));
                    pile[i] = nim;
                }
            }
        } else {//if only one pile remains
            System.out.println("Computer removes " + pile[realPileNum] + " from pile "
                    + (realPileNum + 1));
            pile[realPileNum] = 0;
        }
    }

    /**
     * this method simulates the user's turn, taking in user input to find how many sticks the user
     * wishes to remove from which pile, and removes the sticks.
     * @param pile the array holding the piles and their sticks
     * @param input Scanner to take user input
     */
    public static void userPlay(int[] pile, Scanner input) {
        int pileNum = 0, quantity = 0;
        boolean valid = false;
        while (!valid) {
            System.out.println("\nEnter pile# and quantity removed in that pile:");
            if (input.hasNextInt()) {
                pileNum = input.nextInt();
                if (input.hasNextInt()) {
                    quantity = input.nextInt();
                    if (pileNum - 1 < 0 || pileNum - 1 >= pile.length) {
                        System.out.println("Invalid pile number.");
                    } else if (quantity < 1 || quantity > pile[pileNum - 1]) {
                        System.out.println("Quantity not available in this pile.");
                    } else {
                        valid = true;
                    }
                }
            } else {
                System.out.println("Invalid pile number.");
                input.nextLine();
            }
        }
        pile[pileNum - 1] = pile[pileNum - 1] - quantity;
    }

    /**
     * method displays the number of sticks in each pile, formatted
     * @param pile the array holding the piles and their sticks
     */
    public static void dumpPile(int[] pile) {
        String shown = "";
        for (int i = 0; i < pile.length; i++) {
            for (int k = 0; k < pile[i]; k++) {
                shown += "|";
            }
            System.out.println("Pile " + (i + 1) + "[" + pile[i] + "]\t" + shown);
            shown = "";
        }
    }

    /**
     * this method sums all the sticks in the set of piles and returns the sum
     * @param piles the array holding the piles and their sticks
     * @return the total number of sticks in all the piles
     */
    public static int totalRemainingSticks(int[] piles) {
        int sum = 0;
        for (int i : piles) {
            sum += i;
        }
        return sum;
    }

    /**
     * This method performs nim addition on two given decimal integers, by converting them
     * to binary then using some nim specific addition rules. It then converts the sum back to
     * decimal and returns it
     * @param x a decimal integer to be added
     * @param y a decimal integer to be added
     * @return the sum of the two integers(by nim addition), converted to decimal
     */
    public static int nimAddition(int x, int y) {
        boolean[] xBin = convertToBinary(x);
        boolean[] yBin = convertToBinary(y);
        boolean[] sumBin = new boolean[32];
        for (int i = 0; i < sumBin.length; i++) {
            sumBin[i] = xBin[i] != yBin[i];
        }
        int sum = convertFromBinary(sumBin);
        return sum;
    }

    /**
     * This method takes in a boolean array (false is 0, true is 1), treats it as a binary number
     * and converts it into a decimal number and returns the result.
     * @param num the boolean array representing a binary number
     * @return the decimal representation of the binary number
     */
    public static int convertFromBinary(boolean[] num) {
        int sum = 0;
        int multiplier = 1;
        for (int i = num.length - 1; i >= 0; i--) {
            if (num[i]) {
                sum += multiplier;
            }
            multiplier *= 2;
        }
        return sum;
    }

    /**
     * this method takes in a decimal number and converts it into a boolean array that represents
     * the binary equivalent of the number(where 0 is false and 1 is true)
     * @param num a decimal number to be converted
     * @return boolean array representing the binary equivalent of the decimal number
     */
    public static boolean[] convertToBinary(int num) {
        boolean[] number = new boolean[32];//max int val
        for (int i = number.length - 1; i >= 0; i--) {
            number[i] = num % 2 == 1;
            num /= 2;
        }
        return number;
    }

    /**
     * Reads a given file, determined which records are valid, then converts the valid records into
     * an array while removing and displaying the invalid records as an error.
     * @param file the file to be read
     * @return an array of good records, or null if no good records
     * @throws FileNotFoundException in case the file does not exist
     */
    public static int[] interpretFile(File file) throws FileNotFoundException {
        Scanner fileScan = new Scanner(file);
        String line;
        String[] lineArr;
        int numGoodPiles = 0;
        int goodRecordCount = 0, recordNumber = 1;
        while (fileScan.hasNextLine()) {
            line = fileScan.nextLine();
            line = line.trim();
            if (!isInt(line)) {
                System.out.println("Record #: " + recordNumber + " Error: " + line);
            } else {
                if (goodRecordCount < Integer.MAX_VALUE) {
                    goodRecordCount++;
                }
            }
            recordNumber++;
        }
        int[] records = new int[goodRecordCount];
        int position = 0;
        Scanner fillScan = new Scanner(file);
        while (fillScan.hasNextLine()) {
            line = fillScan.nextLine();
            line = line.trim();
            if (isInt(line)) {
                if (position < Integer.MAX_VALUE) {
                    records[position] = intVal(line);
                    position++;
                }
            }
            recordNumber++;
        }
        for (int i = 0; i < records.length; i++) {
            if (records[i] != 0) {
                numGoodPiles++;
            }
        }
        if (numGoodPiles >= 1) {
            return records;
        } else {
            return null;
        }
    }

    /**
     * Converts a string to an integer
     * @param num the string to convert
     * @return the integer representation of the string
     */
    public static int intVal(String num) {
        int position = 1;
        int sum = 0;
        for (int i = num.length() - 1; i >= 0; i--) {
            sum += Character.getNumericValue(num.charAt(i)) * position;
            position *= 10;
        }
        return sum;
    }

    /**
     * checks whether or not a String holds a single integer
     * @param unknown the String to check
     * @return boolean stating whether or not the String is an integer
     */
    public static boolean isInt(String unknown) {
        int digit = 0;
        for (int i = 0; i < unknown.length(); i++) {
            if (!Character.isDigit(unknown.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method checks that the value entered is a valid filename,
     * and repeats until the filename is valid
     * Precondition: The user inputs something
     * Postcondition: a valid filename is returned
     *
     * @param scan The scanner that takes an input as the filename
     * @param file The name of the file
     * @return The filename that actually exists
     */
    public static String checkValid(Scanner scan, String file) {
        while (!new File(file + ".txt").exists()) {
            System.out.println("File does not exist. Please try again.");
            System.out.print("Enter name of input file - no extension please: ");
            file = scan.next();
        }
        return file;
    }
}
