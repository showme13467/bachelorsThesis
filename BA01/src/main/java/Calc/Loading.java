package Calc;
//code inspired from https://javabeginners.de/Ein-_und_Ausgabe/Eine_Datei_zeilenweise_auslesen.php

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//class for loading the wall coords and saving them into an arraylist
public class Loading {
    public ArrayList<Integer> store = new ArrayList<Integer>();   //coords for walls in gui

    public void loaddata(String datName) {
        File file = new File(datName);

        if (!file.canRead() || !file.isFile())
            System.exit(0);

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(datName));
            String zeile = null;
            while ((zeile = in.readLine()) != null) {
          //      System.out.println(zeile);
                int Intzeile = Integer.parseInt(zeile);
                store.add(Intzeile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
        }
        // System.out.println("Counter: "+store.size());
    }
}