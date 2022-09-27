package com.exist.exercise2;

import java.util.LinkedHashMap;
import java.util.Scanner;

public class Exercise2{

    private static LinkedHashMap<String, String> cellData = new LinkedHashMap<>();
    public static void main(String args[]) {
        var methods = new MethodContainer();
        boolean loop = true;
        do {
            methods.checkExistingFiles();
            cellData = methods.getMatrix();
            int answer;
            Scanner choice = new Scanner(System.in);
            try {
                    System.out.print("[1]Search \n[2]Edit \n[3]Print \n[4]Reset \n[5]Add Row/s \n[6]Add Column/s \n[7]Exit\n\nInput your choice: ");
                    answer = choice.nextInt(); System.out.println();
                    if(answer > 7) {
                            System.out.println("Invalid choice!"); System.out.println();
                    }
                    else {
                        switch (answer) {
                            case 1 -> methods.searchCell(cellData); //Search
                            case 2 -> methods.editCell(cellData); //Edit
                            case 3 -> methods.printMatrix(cellData); //Print
                            case 4 -> cellData = methods.generateTable("reset"); //Reset Matrix
                            case 5 -> methods.addDimensions("rows"); // Add n number of rows
                            case 6 -> methods.addDimensions("cols"); // Add n number of columns
                            case 7 -> loop = false; // Exit
                        }
                    }
            } catch(Exception e) {
                    System.out.println("Invalid number!");
            }
        }while(loop);
        System.out.println("Bye!");
    } 
}
