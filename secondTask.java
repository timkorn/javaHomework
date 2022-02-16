package com.metanit;
import java.util.Scanner;
public class secondTask {
    public static void main(String[] args){
        try {
            Scanner console = new Scanner(System.in);
            System.out.print("Enter the size of matrix:\n");
            String str1 = console.nextLine();
            System.out.print("Enter the matrix:\n");
            String str2 = console.nextLine();
            matrix x = new matrix(str1, str2);
            x.findDet();
            System.out.print("\n");
            System.out.print("Enter the size of another matrix:\n");
            String str3 = console.nextLine();
            System.out.print("Enter the matrix:\n");
            String str4 = console.nextLine();
            matrix y = new matrix(str3, str4);
            matrix z = x.substr(y);
            z.prMatr();
            z = x.mult(y);
            System.out.print("\n");
            z.prMatr();
        }
        catch(NotEqualDimensionsOfMatrixException x){
            System.out.print(x.getMessage());
            System.out.print("Determinant can be only in matrix with equal dimensions.\n");
        }
        catch(IncorrectСountSizeException x){
            System.out.print(x.getMessage());
            System.out.print("Enter 2 dimensions in the size of matrix.\n");
        }
        catch(WrongFormatOfDimensionsException x){
            System.out.print(x.getMessage());
            System.out.print("Type only numbers.\n");
        }
        catch (IncorrectCountOfMatrixElementsException x){
            System.out.print(x.getMessage());
        }
        catch(IncorrectCountOfSummandsInComplexException x){
            System.out.print(x.getMessage());
            System.out.print("Try to check your matrix.\n");
        }
        catch(WrongFormatOfSummandsException x){
            System.out.print(x.getMessage());
            System.out.print("Try to check your matrix.\n");
        }
    }
}
