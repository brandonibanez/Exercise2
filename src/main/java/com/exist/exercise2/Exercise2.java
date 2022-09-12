package com.exist.exercise2;

import java.util.LinkedHashMap;
import java.util.Scanner;

public class Exercise2 extends MethodContainer{
    private static LinkedHashMap<String, String> cellData = new LinkedHashMap<>();
    public static void main(String args[]) {
        boolean loop = true;
        do {
            checkExistingFiles();
            cellData = getMatrix();
            int i=0;
            Scanner choice = new Scanner(System.in);
            try {
                    System.out.print("[1]Search \n[2]Edit \n[3]Print \n[4]Reset \n[5]Add Row/s \n[6]Add Column/s \n[7]Exit\n\nInput your choice: ");
                    i = choice.nextInt(); System.out.println();
                    if(i > 7) {
                            System.out.println("Invalid choice!"); System.out.println();
                    }
                    else {
                            if(i == 1) { //Search
                                searchCell(cellData); System.out.println();
                            }
                            else if(i == 2) { //Edit
                                editCell(cellData);
                                System.out.println("Successful edit! \nMatrix is now saved on a new file...");
                            }
                            else if(i == 3) { //Print
                                printArray(cellData);
                                System.out.println("");
                            }
                            else if(i == 4) {
                                cellData = generateTable("reset");
                                System.out.print("Tables has been populated! If custom save is present it is deleted."); 
                                System.out.println("\n");
                            }
                            else if(i == 5) { //Add rows
                                addDimensions("rows");
                            }
                            else if(i == 6) { //Add columns
                                addDimensions("cols");
                            }
                             else if(i == 7) { //Exit
                                loop = false; 
                                System.out.println("Bye!");
                            }
                    }
            } catch(Exception e) {
                    System.out.println("Invalid number!");
            }
        }while(loop);
    } 
}
