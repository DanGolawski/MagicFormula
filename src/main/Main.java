package main;

import main.MagicFormula;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        String answer = "";
        System.out.println("Do you want to update database file?  yes/no");
        Scanner in = new Scanner(System.in);
        while (!answer.equals("yes") && !answer.equals("no")) {
            answer = in.nextLine();
        }
        MagicFormula magicFormula = new MagicFormula();
        if (answer.equals("yes"))
            magicFormula.updateDatabaseFile();
        else
            magicFormula.calculate();

    }


}
