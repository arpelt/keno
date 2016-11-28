/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keno;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * Write data to file.
 *
 * @author AP
 */
public class Writer {

    public static void writeToFileSet(Set<String> content, String file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            Iterator it = content.iterator();
            while (it.hasNext()) {
                writer.write(it.next() + "\n");
            }
            writer.close();
        } catch (IOException ex) {
            System.err.println("Exception: " + ex.getMessage());
            KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
            KenoForm.statusBar.setText("Error in file");
        }
    }

    public static void writeToFileString(String content, String file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(content + "\n");
            writer.close();
        } catch (IOException ex) {
            System.err.println("Exception: " + ex.getMessage());
            KenoForm.statusBar.setForeground(new java.awt.Color(204, 51, 0));
            KenoForm.statusBar.setText("Error in file");
        }

    }
}
