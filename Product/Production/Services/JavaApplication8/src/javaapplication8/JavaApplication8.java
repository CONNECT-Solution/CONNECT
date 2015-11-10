/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication8;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import static java.lang.System.in;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author achidamb
 */
public class JavaApplication8 {

    private final Pattern TAG_REGEX;

    public JavaApplication8() {
        this.TAG_REGEX = Pattern.compile("<Soap:Envelope>(.+?)</Soap:Envelope>");
    }

    private void readFile() {
        try {
            FileInputStream fstream = new FileInputStream("abc.log");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (br.readLine() != null) {
                if (TAG_REGEX.matcher(strLine).matches()) {
                    System.out.print(("print line: " +));
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] argv) {

    }

}
