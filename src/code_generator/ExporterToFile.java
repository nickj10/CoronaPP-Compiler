package code_generator;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ExporterToFile {
    public static void generateFile(String fileName, ArrayList<String> codeLines){
        FileWriter stringMips;

        try {
            stringMips = new FileWriter("asm_code\\"+fileName.replace("txt", "asm"));//declarar el archivo
            PrintWriter escriu = new PrintWriter(stringMips);//declarar un impresor

            for (String s : codeLines) {
                escriu.println(s);
            }

            stringMips.close();
            System.out.println("\"" + fileName + ".txt\" file generated correctly!");

        } catch (Exception IO) {
            System.out.println("Error! The \".txt\" file could not be generated. \n <Error>: " + IO.getMessage());
        }
    }
}
