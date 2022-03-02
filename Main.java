package com.metanit;
import java.util.Scanner;
import java.util.Arrays;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the name of the input file:\n");
        String s = sc.nextLine();
        try {
            File fileInp = new File(s);
            FileReader fr = new FileReader(fileInp);
            int[] array = new int[52];
            int c;
            int count = 0;
            while ((c = fr.read()) != -1) {
                if (c>=65 && c<=90){
                    array[c-65]+=1;
                }
                if (c>=97 && c<=122){
                    array[c-71]+=1;
                }
            }
            int overall = Arrays.stream(array).sum() ;
            fr.close();
            System.out.println("Enter the name of the output file:\n");
            String str = sc.nextLine();
            File fileOut = new File(str);
            FileWriter fw = new FileWriter(fileOut);
            BufferedWriter bufferWriter = new BufferedWriter(fw);
            for (int i =0; i<52; i++){
                if(i<26){
                    bufferWriter.append((char)(i+65));
                }
                else{
                    bufferWriter.append((char)(i+71));
                }
                bufferWriter.append(":");
                bufferWriter.append(String.valueOf(array[i]));
                bufferWriter.append('\n');
            }
            bufferWriter.append("Overall: ");
            bufferWriter.append(Integer.toString(overall));
            bufferWriter.close();
            fw.close();
        }
        catch(FileNotFoundException x){
            System.out.println("File does not exist.\n");
        }
        catch(IOException x){
            System.out.println("Error\n");
        }
    }
}
