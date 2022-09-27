package com.exist.exercise2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MethodContainer{
    
    private static int numRows;
    private static int numCols;
    private static LinkedHashMap<String, String> cellData = new LinkedHashMap<>();
    private final static String DEFAULT_FILE = "default.txt";
    private final static String SAVED_FILE = "saved.txt";
    private static FileInputStream fis = null;
    private static ObjectInputStream ois = null;
    private static FileOutputStream fos = null;
    private static ObjectOutputStream oos = null;
    private static String fileSelector;

    public LinkedHashMap<String, String> getMatrix() {
        return cellData;
    }
    
    public void checkExistingFiles() {
        if(new File(SAVED_FILE).exists()) {
            System.out.println("File selected: Saved");
            fileSelector = "saved";
            cellData = readFile("saved");
            numRows = Integer.parseInt(cellData.get("rows"));
            numCols = Integer.parseInt(cellData.get("cols"));
        }
        else if(new File(DEFAULT_FILE).exists()) {
            System.out.println("File selected: Default");
            fileSelector = "default";
            cellData = readFile("default");
            numRows = Integer.parseInt(cellData.get("rows"));
            numCols = Integer.parseInt(cellData.get("cols"));
        } else {
            System.out.println("Files are not existing. Creating a default file.\n");
            fileSelector = "default";
            cellData = generateTable("generate");
            System.out.println();
            System.out.println("File selected: Default");
        }
    }
    
    public String generateRandomCharacters(int times) {
        Random rand = new Random();
        int i = rand.nextInt(122-65) + 65 ;
        if(i >= 91 && i <= 96)
            return generateRandomCharacters(times);
        if (times >= 1) {
            return (char) i + generateRandomCharacters(times - 1);
        }
    return "";
    }
    
    public void printMatrix(Map<String, String> matrix) {
        int counter = 1;
        for (Map.Entry<String, String> entry : matrix.entrySet()) {
            if(!entry.getKey().equals("rows") && !entry.getKey().equals("cols")) {
                if(counter == Integer.parseInt(matrix.get("cols"))) {
                    counter = 1;
                    System.out.println(entry.getValue() + " ");
                } else {
                    counter++;
                    System.out.print(entry.getValue() + " ");
                }
            }
        }
        System.out.println();
    }

    public void createFile(LinkedHashMap<String, String> matrix, String filename) {
        try {
            if(filename.equals("default"))
                fos = new FileOutputStream(DEFAULT_FILE);
            else
                fos = new FileOutputStream(SAVED_FILE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(matrix);
        }
        catch (IOException ignored) {
        }
        finally {
            try {
                fos.close();
                oos.close();
            }
            catch (IOException ignored) {
            }
        }
    }
        
    public LinkedHashMap<String, String> readFile(String filename) {
        LinkedHashMap<String, String> matrix = new LinkedHashMap<>();
        try {
            if(filename.equals("default"))
                fis = new FileInputStream(DEFAULT_FILE);
            else
                fis = new FileInputStream(SAVED_FILE);
            ois = new ObjectInputStream(fis);
            matrix = (LinkedHashMap<String, String>)ois.readObject();
        }
        catch (IOException | ClassNotFoundException ignored) {
        }
        finally {
            try {
                fis.close();
                ois.close();
            }
            catch (IOException ignored) {
            }
        }
        return matrix;
    }
    
    public void addDimensions(String dimension) {
        boolean loop = true;
        String dimensionType;
        int dimensionValue;
        do {
            try {
                Scanner in = new Scanner(System.in);
                if(dimension.equals("rows")) {
                    System.out.print("Enter the number of rows you want to add: "); 
                    int rows = in.nextInt();
                    numRows += rows;
                    dimensionType = "rows";
                    dimensionValue = rows;
                    cellData = generateTable("addDimensionRow");
                } else {
                    System.out.print("Enter the number of columns you want to add: "); 
                    int cols = in.nextInt();
                    numCols += cols;
                    dimensionType = "columns";
                    dimensionValue = cols;
                    cellData = generateTable("addDimensionColumn");
                }
                System.out.println("Sucessfully added " + dimensionValue + " " + dimensionType + " and was populated with random characters!");
                loop = false;
            } catch(Exception e) {
                System.out.println("Invalid input!");
            }
        }while(loop);
        System.out.println();
    }
    
    public LinkedHashMap<String, String> generateTable(String operation) {
        LinkedHashMap<String, String> matrix = new LinkedHashMap<>();
        boolean loop = true;
        String key;
        do {
            try {
            Scanner in = new Scanner(System.in);
            if(operation.equals("reset") || operation.equals("generate")) {
                System.out.print("Enter the number of rows: "); numRows = in.nextInt();
                System.out.print("Enter the number of columns: "); numCols = in.nextInt();
                matrix.put("rows", String.valueOf(numRows));
                matrix.put("cols", String.valueOf(numCols));
                for(int i = 1; i <= numRows; i++)
                    for(int j = 1; j <= numCols; j++)
                        matrix.put(("r"+(i)) + ("c"+(j)), generateRandomCharacters(3));
            } else {
                matrix = readFile(fileSelector);
                matrix.replace("rows", String.valueOf(numRows));
                matrix.replace("cols", String.valueOf(numCols));
                for(int i = 1; i <= numRows; i++)
                    for(int j = 1; j <= numCols; j++) {
                        key = ("r"+(i)) + ("c"+(j));
                        matrix.putIfAbsent(key, generateRandomCharacters(3));
                }
            }
            loop = false;
            } catch (Exception e) {
                    System.out.println("Invalid input!");
            }
        }while(loop);
        if(operation.equals("reset")) {
            new File(SAVED_FILE).delete();
            fileSelector = "default";
            System.out.print("Reset is successful!\n\n");
        }
        if(operation.equals("addDimensionColumn"))
            matrix = sortMatrixKeys(matrix);
        createFile(matrix, fileSelector);
        return matrix;
    }

    private LinkedHashMap<String, String> sortMatrixKeys(LinkedHashMap<String, String> matrix) {
        String mapKey;
        LinkedHashMap<String, String> sortedMatrix = new LinkedHashMap<>();
        sortedMatrix.put("rows", String.valueOf(numRows));
        sortedMatrix.put("cols", String.valueOf(numCols));
        for(int i = 1; i <= numRows; i++)
            for(int j = 1; j <= numCols; j++) {
                mapKey = ("r"+(i)) + ("c"+(j));
                sortedMatrix.put(mapKey, matrix.get(mapKey));
            }
        return sortedMatrix;
    }
    
    public void editCell(LinkedHashMap<String, String> matrix) {
            if(!fileSelector.equals("saved"))
                System.out.println("Edit notice: A successful edit will produce a new text file called \"saved\".\n");
            System.out.println("Table dimensions Rows: " + numRows + " Columns: " + numCols + " | Index starting value is 1");
            int x, y;
            String cellString, mapKey;
            boolean loop = true;
            do {
                    boolean innerLoop = true;
                    Scanner choice = new Scanner(System.in) ,input = new Scanner(System.in);
                    try {
                            System.out.print("Enter value for row: "); x = choice.nextInt();
                            System.out.print("Enter value for column: "); y = choice.nextInt();
                            mapKey = ("r"+(x)) + ("c"+(y));
                            if(!matrix.containsKey(mapKey)) {
                                System.out.println("Invalid dimension!"); System.out.println();
                            }
                            else {
                                do {
                                    try {
                                        System.out.print("Input anything: ");
                                        cellString = input.nextLine();
                                        if(!cellString.equals("")) {
                                            cellString = cellString.replaceAll("\\s+","");
                                            matrix.replace(mapKey, cellString);
                                            loop = false;
                                            innerLoop = false;
                                        }
                                        else
                                            System.out.println("Input is empty!");
                                    } catch(Exception e) {
                                            System.out.println("Invalid input!");
                                    }
                                }while(innerLoop);
                            }
                    } catch(Exception e) {
                        System.out.println("Invalid input!");
                    }
            }while(loop);
            createFile(matrix, "saved");
            System.out.println();
    }

    public void searchCell(Map<String, String> matrix) {
        String searchString;
        Scanner input = new Scanner(System.in);
        boolean loop = true; boolean searchStringCounted = false;
        do {
            try {
                    System.out.print("Input anything: ");
                    searchString = input.nextLine();
                    if(!searchString.equals("")) {
                        searchString = searchString.replaceAll("\\s+","");
                            for(int i = 1; i <= numRows; i++) {
                                for(int j = 1; j <= numCols; j++) {
                                    String key = (("r"+(i))) + (("c"+(j)));
                                    if(matrix.get(key).contains(searchString))
                                        if (matrix.get(key).length() < searchString.length()) {
                                        } else {
                                            String str = matrix.get(key);
                                            int occurence = str.split(searchString, -1).length - 1;
                                            System.out.println("Occurence of " + searchString + " on [" + i + ", " + j + "] is " + occurence + " time/s");
                                            searchStringCounted = true;
                                        }
                                }
                            }
                        if(!searchStringCounted)
                            System.out.println("No occurence of the input String");
                        loop = false;
                    }
                    else
                        System.out.println("Input is empty!");
            } catch(Exception e) {
                System.out.println("Invalid input!");
                loop = false;
            }
        }while(loop);
        System.out.println();
    }
}
