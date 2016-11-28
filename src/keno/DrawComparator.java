/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keno;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Reads and sorts draw result files.
 *
 * @author AP
 */
class DrawComparator implements Comparator<String> {

    @Override
    public int compare(String line1, String line2) {
        String stringArray1[] = line1.split(",");
        String stringArray2[] = line2.split(",");
        Integer intArray1[] = new Integer[stringArray1.length];
        Integer intArray2[] = new Integer[stringArray2.length];
        intArray1[0] = Integer.parseInt(stringArray1[0]);
        intArray2[0] = Integer.parseInt(stringArray2[0]);

        return intArray1[0].compareTo(intArray2[0]);
    }

    /*Reads the result file which may contain duplicate draws */
    public static void readResultFile() {
        File resultFile2 = new File("keno_result_file2.txt");
        BufferedReader reader = null;
        ArrayList<String> draws = new ArrayList<>();
        String line = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(resultFile2), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Exception: " + ex.getMessage());
        } catch (FileNotFoundException ex) {
            keno.Keno.statusBarTextErrorFileNotFound(ex);
        }
        try {
            while ((line = reader.readLine()) != null) {
                draws.add(line);
            }
            reader.close();
        } catch (IOException ex) {
            System.err.println("Exception: " + ex.getMessage());
            KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
            KenoForm.statusBar.setText("Error in file");
        }

        sortDrawsByDrawId(draws);
    }

    /*Sort draws by Draw ID and remove duplicate lines*/
    protected static void sortDrawsByDrawId(ArrayList<String> draws) {
        String resultFile1 = "keno_result_file1.txt";
        Collections.sort(draws, new DrawComparator());
        Set<String> setDraws = new LinkedHashSet<String>(draws);
        Writer.writeToFileSet(setDraws, resultFile1);
        KenoForm.statusBar.setForeground(new java.awt.Color(13, 76, 5));
        KenoForm.statusBar.setText("Done");
    }
}
