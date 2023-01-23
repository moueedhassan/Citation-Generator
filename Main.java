import com.sun.source.tree.PackageTree;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;


class FileInvalidException extends Exception{
    FileInvalidException(){
        super("Error: Input file cannot be parsed due to missing information (i.e. month={}, title={}, etc.)");
    }
    FileInvalidException(String message){
        super(message);
    }
}


public class Main {



    public static void main(String[] args) {

        System.out.println("Welcome to BibliographyFactory!");

        Scanner inputByUser = new Scanner(System.in);
        Scanner[] scannerlatex = new Scanner[10];
        String[] LatexString = new String[10];

        int invalidFileCount = 0;

        //creating .bib names for file opening
        for (int i = 0; i < 10; i++) {
            LatexString[i] = "Latex" + (i + 1) + ".bib";
        }


        //creating json names
        String[] IEEEjson = new String[10];
        String[] ACMjson = new String[10];
        String[] NJjson = new String[10];

        for (int i = 0; i < 10; i++) {
            IEEEjson[i] = "IEEE" + (i + 1) + ".json";
            ACMjson[i] = "ACM" + (i + 1) + ".json";
            NJjson[i] = "NJ" + (i + 1) + ".json";
        }

        PrintWriter[] ieeePw = new PrintWriter[10];
        PrintWriter[] acmPw = new PrintWriter[10];
        PrintWriter[] njPw = new PrintWriter[10];
        int num2 = 0;

        int filenum = 0;
        String fileName = "";
        int numberOfInvalidFiles = 0;
        for (int i = 0; i < 10; i++) {
            //opening files
            try {
                fileName = LatexString[i];
                scannerlatex[i] = new Scanner(new FileInputStream(fileName));
                filenum = i;
            } catch (FileNotFoundException e1) {
                System.out.println("Could not open input file " + fileName + " for reading. Please check if file exists! Program will terminate after closing any opened files");

                for (int j = 0; j < filenum; j++) {
                    scannerlatex[j].close();
                }
                System.exit(0);
            }

            try {
                File myobj = new File(IEEEjson[i]);
                ieeePw[i] = new PrintWriter(new FileOutputStream(IEEEjson[i]));
                num2++;
            } catch (FileNotFoundException e2) {
                System.out.println("The file" + ieeePw[i] + "could not be created.");
                for (int j = 0; j < num2; j++) {
                    File f = new File(IEEEjson[j]);
                    f.delete();
                }

                for (int k = 0; k < 10; k++) {
                    scannerlatex[k].close();
                }
                System.exit(0);
            }


            try {
                File myobj = new File(ACMjson[i]);
                acmPw[i] = new PrintWriter(new FileOutputStream(ACMjson[i]));
                num2++;

            } catch (FileNotFoundException e3) {
                System.out.println("The file" + acmPw[i] + "could not be created.");
                for (int j = 0; j < num2; j++) {
                    File f = new File(ACMjson[j]);
                    f.delete();
                }

                for (int k = 0; k < 10; k++) {
                    scannerlatex[k].close();
                }
                System.exit(0);
            }

            try {
                File myobj = new File(NJjson[i]);
                njPw[i] = new PrintWriter(new FileOutputStream(NJjson[i]));
                num2++;

            } catch (FileNotFoundException e3) {
                System.out.println("The file" + njPw[i] + "could not be created.");
                for (int j = 0; j < num2; j++) {
                    File f = new File(NJjson[j]);
                    f.delete();
                }

                for (int k = 0; k < 10; k++) {
                    scannerlatex[k].close();
                }
                System.exit(0);
            }


            boolean validOrInvalid = BibliographyGenerator.processFilesforValidation(scannerlatex, acmPw, ieeePw, njPw, i);
            if(!validOrInvalid){
                numberOfInvalidFiles++;
            }
        }

        System.out.println("A total of "+numberOfInvalidFiles+" files were found to be invalid. All other "+(10-numberOfInvalidFiles)+" \"Valid\" files have been created");
        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.println();
        int count = 1;
        while (count <= 2){
            System.out.println("Please enter the name of one of the files you need to review: ");
            String userFileName = inputByUser.next();

            try{
                BufferedReader br;
                br = new BufferedReader(new FileReader(userFileName));
                String s = br.readLine();
                int i = 1;
                while(s!=null){
                    System.out.println("["+i+"]"+"\t" + s);
                    System.out.println();
                    i++;
                    s = br.readLine();
                }
                break;
            }

            catch (FileNotFoundException e3){
                if(count == 1){
                    System.out.println("File Not Found, You have another chance");
                }
                count++;
            }
            catch (IOException e4){
                if(count==1){
                    System.out.println("Wrong Input, You have another chance");
                }
                count++;
            }
        }
    }
}
















