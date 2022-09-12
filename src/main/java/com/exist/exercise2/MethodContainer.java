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

public class MethodContainer {
    
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

    public static LinkedHashMap<String, String> getMatrix() {
        return cellData;
    }
    
    public static void checkExistingFiles() {
        if(new File(SAVED_FILE).exists()) {
            fileSelector = "saved";
            cellData = readFile("saved");
            numRows = Integer.parseInt(cellData.get("rows"));
            numCols = Integer.parseInt(cellData.get("cols"));
        }
        else if(new File(DEFAULT_FILE).exists()) {
            fileSelector = "default";
            cellData = readFile("default");
            numRows = Integer.parseInt(cellData.get("rows"));
            numCols = Integer.parseInt(cellData.get("cols"));
        } else {
            fileSelector = "default";
            cellData = generateTable("generate");
        }
    }
    
    public static String generateRandomCharacters(int times) {
        Random rand = new Random();
        int i = rand.nextInt(122-65) + 65 ;
        if(i >= 91 && i <= 96)
            return generateRandomCharacters(times);
        if (times >= 1) {
            return Character.toString((char) i) + generateRandomCharacters(times - 1);
        }
    return "";
    }

    public static void printArray(Map<String, String> map) {
        int counter = 1;
        for(int i = 1; i <= numRows; i++)
            for(int j = 1; j <= numCols; j++) {
                String key = (("r"+(String.valueOf(i)))) + (("c"+(String.valueOf(j))));
                if(counter == Integer.parseInt(map.get("cols"))) {
                    counter = 1;
                    System.out.println(map.get(key) + " ");
                }
                else {
                    counter++;
                    System.out.print(map.get(key) + " ");
                }
            }
    }

    public static void createFile(LinkedHashMap<String, String> map, String s) {
        try {
            if(s.equals("default"))
                fos = new FileOutputStream(DEFAULT_FILE);
            else
                fos = new FileOutputStream(SAVED_FILE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fos.close();
                oos.close();
            }
            catch (Exception e) {
            }
        }
    }
        
    public static LinkedHashMap<String, String> readFile(String s) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        try {
            if(s.equals("default"))
                fis = new FileInputStream(DEFAULT_FILE);
            else
                fis = new FileInputStream(SAVED_FILE);
            ois = new ObjectInputStream(fis);
            map = (LinkedHashMap<String, String>)ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                fis.close();
                ois.close();
            }
            catch (Exception e) {
            }
        }
        return map;
    }
    
    public static void addDimensions(String s) {
        boolean loop = true;
        String dimensionType;
        int dimensionValue;
        do {
            try {
                Scanner in = new Scanner(System.in);
                if(s.equals("rows")) {
                    System.out.print("Enter the number of rows you want to add: "); 
                    int rows = in.nextInt();
                    numRows += rows;
                    dimensionType = "rows";
                    dimensionValue = rows;
                } else {
                    System.out.print("Enter the number of columns you want to add: "); 
                    int cols = in.nextInt();
                    numCols += cols;
                    dimensionType = "columns";
                    dimensionValue = cols;
                }
                cellData = generateTable("addDimension");
                System.out.println("Sucessfully added " + dimensionValue + " " + dimensionType + " and was populated with random characters!");
                loop = false;
            } catch(Exception e) {
                System.out.println("Invalid input!");
            }
        }while(loop);
    }
    
    public static LinkedHashMap<String, String> generateTable(String s) {
        LinkedHashMap<String, String> matrix = new LinkedHashMap<>();
        boolean loop = true;
        String key;
        do {
            try {
            Scanner in = new Scanner(System.in);
            if(!s.equals("addDimension")) {
                System.out.print("Enter the number of rows: "); numRows = in.nextInt();
                System.out.print("Enter the number of columns: "); numCols = in.nextInt();
                matrix.put("rows", String.valueOf(numRows));
                matrix.put("cols", String.valueOf(numCols));
                for(int i = 1; i <= numRows; i++)
                    for(int j = 1; j <= numCols; j++)
                        matrix.put(("r"+(String.valueOf(i))) + ("c"+(String.valueOf(j))), generateRandomCharacters(3));
            } else {
                matrix = readFile(fileSelector);
                matrix.replace("rows", String.valueOf(numRows));
                matrix.replace("cols", String.valueOf(numCols));
                for(int i = 1; i <= numRows; i++)
                    for(int j = 1; j <= numCols; j++) {
                        key = ("r"+(String.valueOf(i))) + ("c"+(String.valueOf(j)));
                        matrix.putIfAbsent(key, generateRandomCharacters(3));
                }
            }
            loop = false;
            } catch (Exception e) {
                    System.out.println("Invalid input!");
            }
        }while(loop);
        createFile(matrix, fileSelector);
        if(s.equals("reset"))
            new File(SAVED_FILE).delete();
        return matrix;
    }

    public static void editCell(LinkedHashMap<String, String> map) { // Need saving
            System.out.println("Table dimensions Row/s: " + numRows + " Column/s: " + numCols + " | Index starting value is 1");
            int x = 0, y = 0;
            String cellString = "", mapKey;
            boolean loop = true;
            do {
                    boolean innerLoop = true;
                    Scanner choice = new Scanner(System.in) ,input = new Scanner(System.in);
                    try {
                            System.out.print("Enter value for row: "); x = choice.nextInt();
                            System.out.print("Enter value for column: "); y = choice.nextInt();
                            mapKey = ("r"+(String.valueOf(x))) + ("c"+(String.valueOf(y)));
                            if(!map.containsKey(mapKey)) {
                                System.out.println("Invalid dimension!"); System.out.println();
                            }
                            else {
                                do {
                                    try {
                                        System.out.print("Input anything: ");
                                        cellString = input.nextLine();
                                        if(!cellString.equals("")) {
                                            cellString = cellString.replaceAll("\\s+","");
                                            map.replace(mapKey, cellString);
                                            System.out.println("Edit success!"); System.out.println();
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
            createFile(map, "saved");
    }

    public static void searchCell(Map<String, String> map) {
        String searchString = "";
        Scanner input = new Scanner(System.in);
        boolean loop = true; boolean searchStringCounted = false;
        do {
                try {
                        System.out.print("Input anything: ");
                        searchString = input.nextLine();
                        if(!searchString.equals("")) {
                            boolean innerLoop = true;
                            searchString = searchString.replaceAll("\\s+","");
                                for(int i = 1; i <= numRows; i++) {
                                        for(int j = 1; j <= numCols; j++) {
                                            String key = (("r"+(String.valueOf(i)))) + (("c"+(String.valueOf(j))));
                                            if(map.get(key).contains(searchString)) {
                                                if(map.get(key).length() < searchString.length())
                                                    continue;
                                                else {
                                                    String str = map.get(key);
                                                    String findStr = searchString;
                                                    int occurence = str.split(findStr, -1).length-1;
                                                    System.out.println("Occurence of " + findStr + " on [" + i + ", " + j + "] is " + occurence + " time/s");
                                                    searchStringCounted = true;
                                                }
                                            }
                                            else
                                                continue;
                                        }
                                if(!innerLoop)
                                        break;
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
    }
}
