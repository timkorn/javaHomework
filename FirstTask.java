package com.metanit;
import com.metanit.ComplexNumber;
import java.lang.Math;
import java.util.Scanner;
public class FirstTask {
    public static void main(String[] args){
            try {
                    Scanner console = new Scanner(System.in);
                    String str1 = console.nextLine();
                    ComplexNumber x = new ComplexNumber(str1);
                    x.out("algebraic");
                    System.out.print("\n");
                    x.out("trigonometric");
                    System.out.print("\n");
                    String str2 = console.nextLine();
                    ComplexNumber y = new ComplexNumber(str1);
                    ComplexNumber z = x.substr(y);
                    z.out("default");
                    System.out.print("\n");
                    z = x.multipl(y);
                    z.out("default");
                    System.out.print("\n");
                    z.prModule();
                    z.prArg();
                    z = z.division(y);
                    z.out("default");
            }
            catch(IncorrectCountOfSummandsInComplexException x){
                    System.out.print(x.getMessage());
            }
            catch(WrongFormatOfSummandsException x){
                    System.out.print(x.getMessage());
            }

    }
}
