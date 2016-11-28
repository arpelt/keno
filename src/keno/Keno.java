/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keno;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 * Implements Keno application.
 *
 * @author AP
 */
public class Keno {

    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {

        }
        KenoForm form = new KenoForm();
        form.setVisible(true);
    }

    /**
     * Reads the last line from the result file.
     *
     * @return a string containing the last draw line.
     * @throws java.io.IOException
     */
    public static String readLastDrawLine() throws IOException {
        BufferedReader reader = readDataFromFile();
        String line = "";
        String lastDraw = "";
        try {
            while ((line = reader.readLine()) != null) {
                lastDraw = line;
            }
        } catch (IOException ex) {
            System.err.println("IOException: " + ex);
            KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
            KenoForm.statusBar.setText("Error in file");
        } finally {
            if (lastDraw.equals("")) {
                KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
                KenoForm.statusBar.setText("Error in file");
                throw new NullPointerException("The last line in result file cannot be empty. ");
            }
            reader.close();
        }
        return lastDraw;
    }

    /**
     * Parse text line to the string array and returns an array. The first
     * element of an array is the last draw id (reserved for later usage). The
     * second element of an array is the last draw date.
     *
     * @return an array of strings.
     */
    public static String[] lastDraw() {
        String lastDrawIdAndDate[] = new String[2];
        String lastDrawDate = "";
        String lastDrawId = ""; // Variable lastDrawId is not used (reserved for later usage).
        try {
            lastDrawDate = readLastDrawLine();
        } catch (IOException ex) {
            System.err.println("IOException: " + ex);
            KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
            KenoForm.statusBar.setText("Error in file");
        }

        lastDrawId = lastDrawDate;

        Pattern datePattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d)");
        Matcher dateMatcher = datePattern.matcher(lastDrawDate);

        Pattern drawPattern = Pattern.compile("(\\d+{1}+)");
        Matcher drawmatcher = drawPattern.matcher(lastDrawId);

        if (drawmatcher.find()) {
            lastDrawId = drawmatcher.group(1);
            lastDrawIdAndDate[0] = lastDrawId;
        }

        if (dateMatcher.find()) {
            lastDrawDate = dateMatcher.group(1);
            lastDrawIdAndDate[1] = lastDrawDate;
        }
        
        if (lastDrawIdAndDate[0] == null || lastDrawIdAndDate[1] == null) {
            KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
            KenoForm.statusBar.setText("Error in file");
            throw new NullPointerException("Array lastDrawIdAndDate cannot be null.");
        } else {
            return lastDrawIdAndDate;
        }
    }

    /**
     * Parses each draw line from the result file and returns an array.
     *
     * @return an array which contains draw data.
     */
    public static ArrayList<String> parseNumbers() {
        BufferedReader reader = readDataFromFile();
        String primaryAndSecondaryNumbers = "";
        String line = "";
        String replacement = "";
        String delimiter = "[ ,\\[\\]\\{\\}\":]+";
        ArrayList<String> primaryAndSecondaryNumbersDrawIdDate = new ArrayList<>();
        String[] numbers = primaryAndSecondaryNumbers.split(delimiter);
        try {
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    int startIndex = line.indexOf("(d)");
                    int endIndex = line.indexOf("[{");
                    String drawId = line.split(",")[0];
                    String drawDate = line.split(",")[1];
                    String toBeReplaced = line.substring(startIndex + 1, endIndex);
                    primaryAndSecondaryNumbers = line.replace(toBeReplaced, replacement);
                    numbers = primaryAndSecondaryNumbers.split(delimiter);
                    primaryAndSecondaryNumbersDrawIdDate.add(drawDate);
                    primaryAndSecondaryNumbersDrawIdDate.add(drawId);
                    Collections.addAll(primaryAndSecondaryNumbersDrawIdDate, numbers);
                }

            }
        } catch (IOException ex) {
            System.err.println("IOException: " + ex);
            KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
            KenoForm.statusBar.setText("Error in file");
        } catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException ex) {
            System.err.println("Exception: " + ex);
            KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
            KenoForm.statusBar.setText("Error in file");
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                System.err.println("IOException: " + ex);
                KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
                KenoForm.statusBar.setText("Error in file");
            }

        }
        return primaryAndSecondaryNumbersDrawIdDate;
    }

    /**
     * Compares each draw to player's selected numbers.
     *
     * @param kenoNbrsSelected a player's selected numbers between 1 to 70.
     * Player can select 2 to 10 numbers.
     * @throws java.io.UnsupportedEncodingException
     */
    public static void compareUserAndDrawNumbers(ArrayList<String> kenoNbrsSelected) throws UnsupportedEncodingException {
        try {
            ArrayList<String> primaryNumbers = new ArrayList<>();
            ArrayList<String> allNumbers = parseNumbers();
            String kenoDate = "Not available"; //initial value for statusBar
            int correctNumber = 0;
            int zero = 0;
            int one = 0;
            int two = 0;
            int three = 0;
            int four = 0;
            int five = 0;
            int six = 0;
            int seven = 0;
            int eight = 0;
            int nine = 0;
            int ten = 0;
            int drawCounter = 0;
            int arraySize = allNumbers.size();
            for (int i = 0; i < arraySize; i++) {
                String value = allNumbers.get(i);
                if (value.contains("Keno")) {
                    kenoDate = allNumbers.get(i);
                    i++;
                    //kenoDrawId = allNumbers.get(i);
                }
                if (value.contains("primary")) {
                    drawCounter++;
                    try {
                        for (int j = 0; j < 20; j++) {
                            i++;
                            String primaryValue = allNumbers.get(i);
                            primaryNumbers.add(primaryValue);
                            correctNumber = countCorrectNumbers(kenoNbrsSelected, primaryValue, correctNumber);
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        System.err.println("IndexOutOfBoundsException: " + ex.getMessage());
                        KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
                        KenoForm.statusBar.setText("Error in file");
                    }
                    if (correctNumber == 0) {
                        zero++;
                    }
                    if (correctNumber == 1) {
                        one++;
                    }
                    if (correctNumber == 2) {
                        two++;
                    }
                    if (correctNumber == 3) {
                        three++;
                    }
                    if (correctNumber == 4) {
                        four++;
                    }
                    if (correctNumber == 5) {
                        five++;
                    }
                    if (correctNumber == 6) {
                        six++;
                    }
                    if (correctNumber == 7) {
                        seven++;
                    }
                    if (correctNumber == 8) {
                        eight++;
                    }
                    if (correctNumber == 9) {
                        nine++;
                    }
                    if (correctNumber == 10) {
                        ten++;
                    }

                    if (KenoForm.jCheckBox2State) {
                        printCorrectNumbersAmount("Correct: " + correctNumber + " " + kenoDate + " " + primaryNumbers + "\n");
                        primaryNumbers.clear();
                    }
                }
                correctNumber = 0;
                //Secondary number checking...
                //if (value.contains("secondary")) {
                //    for (int j = 0; j < 1; j++) {
                //        i++;
                //        String secondaryValue = allNumbers.get(i);
                //        System.out.println("Seondary Number " + secondaryValue);
                //    }
                //}
            }
            KenoForm.statusBar.setText("The total number of draws: " + drawCounter + "  -  " + "The latest draw: " + kenoDate);

            if (KenoForm.jCheckBox1State) {
                printCorrectNumbersAmount("_______________________________________________________\n");
                printCorrectNumbersAmount(" 0 - numbers correct \t" + zero + "\n");
                printCorrectNumbersAmount(" 1 - number correct \t" + one + "\n");
                printCorrectNumbersAmount(" 2 - numbers correct \t" + two + "\n");
                printCorrectNumbersAmount(" 3 - numbers correct \t" + three + "\n");
                printCorrectNumbersAmount(" 4 - numbers correct \t" + four + "\n");
                printCorrectNumbersAmount(" 5 - numbers correct \t" + five + "\n");
                printCorrectNumbersAmount(" 6 - numbers correct \t" + six + "\n");
                printCorrectNumbersAmount(" 7 - numbers correct \t" + seven + "\n");
                printCorrectNumbersAmount(" 8 - numbers correct \t" + eight + "\n");
                printCorrectNumbersAmount(" 9 - numbers correct \t" + nine + "\n");
                printCorrectNumbersAmount("10 - numbers correct \t" + ten + "\n");
                printCorrectNumbersAmount("_______________________________________________________\n");
                printCorrectNumbersAmount("Numbers: \t" + kenoNbrsSelected + "\n");
            }
        } catch (BadLocationException ex) {
            System.err.println("Exception: " + ex);
            KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
            KenoForm.statusBar.setText("Error in file");
        }
    }

    /**
     * Count correct number amount.
     *
     * @param kenoNbrsSelected a player's selected numbers between 1 and 70.
     * player can select 2 to 10 numbers.
     * @param primaryValue a number between 1 and 70.
     * @param correctNumber a player's selected number match with draw number.
     * @return
     */
    public static int countCorrectNumbers(ArrayList<String> kenoNbrsSelected, String primaryValue, int correctNumber) {
        if (kenoNbrsSelected.size() >= 1 && primaryValue.equals(kenoNbrsSelected.get(0))) {
            correctNumber++;
        }
        if (kenoNbrsSelected.size() > 1 && primaryValue.equals(kenoNbrsSelected.get(1))) {
            correctNumber++;
        }
        if (kenoNbrsSelected.size() > 2 && primaryValue.equals(kenoNbrsSelected.get(2))) {
            correctNumber++;
        }
        if (kenoNbrsSelected.size() > 3 && primaryValue.equals(kenoNbrsSelected.get(3))) {
            correctNumber++;
        }
        if (kenoNbrsSelected.size() > 4 && primaryValue.equals(kenoNbrsSelected.get(4))) {
            correctNumber++;
        }
        if (kenoNbrsSelected.size() > 5 && primaryValue.equals(kenoNbrsSelected.get(5))) {
            correctNumber++;
        }
        if (kenoNbrsSelected.size() > 6 && primaryValue.equals(kenoNbrsSelected.get(6))) {
            correctNumber++;
        }
        if (kenoNbrsSelected.size() > 7 && primaryValue.equals(kenoNbrsSelected.get(7))) {
            correctNumber++;
        }
        if (kenoNbrsSelected.size() > 8 && primaryValue.equals(kenoNbrsSelected.get(8))) {
            correctNumber++;
        }
        if (kenoNbrsSelected.size() > 9 && primaryValue.equals(kenoNbrsSelected.get(9))) {
            correctNumber++;
        }
        return correctNumber;
    }

    /**
     * Displays text information.
     *
     * @param str string value
     * @throws javax.swing.text.BadLocationException
     */
    public static void printCorrectNumbersAmount(String str) throws BadLocationException {
        StyledDocument document = (StyledDocument) KenoForm.jTextPane2.getDocument();
        document.insertString(document.getLength(), str, null);
    }

    /**
     * Counts how many times each number (1-70) appears in draws.
     *
     * @return two dimensional array
     * @throws java.io.UnsupportedEncodingException
     * @throws javax.swing.text.BadLocationException
     */
    public static int[][] countNumbersOneToSeventyAmount() throws UnsupportedEncodingException, BadLocationException {
        ArrayList<String> allNumbers = parseNumbers();
        ArrayList<String> numbersOneToSeventy = new ArrayList<>();
        arrayNumbersOneToSeventy(numbersOneToSeventy);
        int values[][] = new int[70][2];
        int allNumbersArraySize = allNumbers.size();
        int amount = 0;
        int numberOneSeventy = 1;
        int numbersOneToSeventyArraySize = numbersOneToSeventy.size();
        for (int j = 0; j < numbersOneToSeventyArraySize; j++) {
            for (int x = 0; x < allNumbersArraySize; x++) {
                if (allNumbers.get(x).contains("primary")) {
                    for (int y = 0; y < 20; y++) {
                        x++;
                        if (allNumbers.get(x).equals(numbersOneToSeventy.get(j))) {
                            amount++;
                        }
                    }
                }
            }
            values[j] = new int[2];
            values[j][0] = numberOneSeventy;
            values[j][1] = amount;
            numberOneSeventy++;
            amount = 0;
        }
        return values;
    }

    /**
     * Counts numbers that have/haven't hit.
     *
     * @return two dimensional array
     */
    public static int[][] hotAndColdNumbers() {
        boolean correctNumber = false;
        int coldCounter = 0;
        int hotCounter = 0;
        int numberOneSeventy = 1;
        int hotAndCold[][] = new int[70][2];
        ArrayList<String> allNumbers = parseNumbers();
        ArrayList<String> kenoNbrs = new ArrayList<>();
        arrayNumbersOneToSeventy(kenoNbrs);
        int arraySize = allNumbers.size();
        for (int m = 0; m < kenoNbrs.size(); m++) {
            for (int i = 0; i < arraySize; i++) {
                String value = allNumbers.get(i);
                if (value.contains("primary")) {
                    for (int j = 0; j < 20; j++) {
                        i++;
                        String primaryValue = allNumbers.get(i);
                        if (primaryValue.equals(kenoNbrs.get(m))) {
                            correctNumber = true;
                        }
                    }
                    if (!correctNumber) {
                        coldCounter++;
                        hotCounter = 0;
                    }
                    if (correctNumber) {
                        hotCounter++;
                        coldCounter = 0;
                    }
                }
                correctNumber = false;
            }
            hotAndCold[m] = new int[2];
            hotAndCold[m][0] = numberOneSeventy;
            numberOneSeventy++;
            if (coldCounter > hotCounter) {
                hotAndCold[m][1] = coldCounter - coldCounter - coldCounter;
                coldCounter = 0;
            }
            if (hotCounter > coldCounter) {
                hotAndCold[m][1] = hotCounter;
                hotCounter = 0;
            }
        }
        return hotAndCold;
    }

    /**
     * Displays the numbers in the two dimensional array.
     *
     * @param tableName table name
     */
    public static void printNumbersToTable(String tableName) {
        int numbers[][] = new int[70][2];
        try {
            if (tableName.equals("numbersOneToSeventy")) {
                numbers = Keno.countNumbersOneToSeventyAmount();
            }
            if (tableName.equals("hotAndColdNumbers")) {
                numbers = Keno.hotAndColdNumbers();
            }
        } catch (UnsupportedEncodingException | BadLocationException ex) {
            Logger.getLogger(KenoForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < numbers.length; i++) {
            if (tableName.equals("numbersOneToSeventy")) {
                KenoForm.jTableOneToSeventyNumbers.getModel().setValueAt(numbers[i][0], i, 0);
            }
            if (tableName.equals("hotAndColdNumbers")) {
                KenoForm.jTableHotColdNumbers.getModel().setValueAt(numbers[i][0], i, 0);
            }
            for (int j = 0; j < numbers.length; j++) {
                if (tableName.equals("numbersOneToSeventy")) {
                    KenoForm.jTableOneToSeventyNumbers.getModel().setValueAt(numbers[j][1], j, 1);
                }
                if (tableName.equals("hotAndColdNumbers")) {
                    KenoForm.jTableHotColdNumbers.getModel().setValueAt(numbers[j][1], j, 1);
                }
            }
        }
    }

    /**
     * BufferedReader
     *
     * @return reader
     */
    public static BufferedReader readDataFromFile() {
        String drawFile = "keno_result_file1.txt";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(drawFile), "UTF-8")); //ISO-8859-1
        } catch (FileNotFoundException ex) {
            statusBarTextErrorFileNotFound(ex);
        } catch (UnsupportedEncodingException ex) {
            System.err.println("UnsupportedEncodingException: " + ex.getMessage());
        }
        if (reader == null) {
            throw new NullPointerException("BufferedReader cannot be null. ");
        } else {
            return reader;
        }
    }

    /**
     * Displays an exception in the status bar.
     *
     * @param ex
     */
    protected static void statusBarTextErrorFileNotFound(FileNotFoundException ex) {
        System.err.println("FileNotFoundException: " + ex);
        KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
        KenoForm.statusBar.setText("File Not Found: " + ex.getMessage());
        //TODO remove below code from this method...
        String str = "Copy both data files 'keno_result_file1.txt' and 'keno_result_file2.txt' to the " + System.getProperty("user.dir") + " folder.";
        StyledDocument document = (StyledDocument) KenoForm.jTextPane2.getDocument();
        try {
            document.insertString(document.getLength(), str, null);
        } catch (BadLocationException ex1) {
            Logger.getLogger(Keno.class.getName()).log(Level.SEVERE, null, ex1);
        }
  
    }

    /**
     * Converts string to integer number.
     *
     * @param allkenoNbrs string numbers 1 to 70
     */
    public static void arrayNumbersOneToSeventy(ArrayList<String> allkenoNbrs) {
        for (int i = 1; i <= 70; i++) {
            String number = Integer.toString(i);
            allkenoNbrs.add(number);
        }
    }
}
