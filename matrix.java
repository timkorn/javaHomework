package com.metanit;
import com.metanit.ComplexNumber;

import java.util.concurrent.CancellationException;

public class matrix {
    private ComplexNumber matr[][];
    private int columnsCount;
    private int rowsCount;
    private matrix(ComplexNumber[][] m,int cols,int rows){
        this.matr = m;
        this.columnsCount= cols;
        this.rowsCount = rows;
    }
    public matrix(String size, String numbers) throws IncorrectСountSizeException,WrongFormatOfDimensionsException,IncorrectCountOfMatrixElementsException,IncorrectCountOfSummandsInComplexException,WrongFormatOfSummandsException{
        String[] s = size.split(" ");
        if(s.length!=2){
            throw new IncorrectСountSizeException("Incorrect count of dimensions.\n");
        }
        try {
            this.rowsCount = Integer.parseInt(s[0]);
            this.columnsCount = Integer.parseInt(s[1]);
            if (rowsCount<=0 || columnsCount<=0){
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException x){
            throw new WrongFormatOfDimensionsException("Dimensions are  ot in the right format.\n");
        }
        matr = new ComplexNumber[rowsCount][columnsCount];
        String[] nums = numbers.split(" ");
        if (nums.length!=rowsCount*columnsCount){
            throw new IncorrectCountOfMatrixElementsException("You entered incorrect count of matrix elements");
        }
        for(int i=0; i<this.rowsCount; i++){
            for(int j=0; j<this.columnsCount; j++){
                this.matr[i][j] = new ComplexNumber(nums[i*columnsCount+j]);
            }
        }
    }
    public matrix substr(matrix other){
        if (this.columnsCount == other.columnsCount && this.rowsCount == other.rowsCount){
            ComplexNumber[][] newm = new ComplexNumber [this.rowsCount][this.columnsCount];
            for(int i=0; i<this.rowsCount; i++){
                for(int j=0; j<this.columnsCount; j++){
                    newm[i][j] = this.matr[i][j].substr(other.matr[i][j]);
                }
            }
            return new matrix(newm,columnsCount,rowsCount);
        }
        else{
            System.out.print("Can not do subtraction\n");
            return this;
        }
    }
    private ComplexNumber onePositionMult(ComplexNumber[][] first, ComplexNumber[][] second,int length,int i, int j,int position){
        if(position!=length-1){
            return first[i][position].multipl(second[position][j]).substr(onePositionMult(first,second,length,i,j,++position));
        }
        else{
            return first[i][position].multipl(second[position][j]);
        }
    }
    public matrix mult(matrix other){
        int newRowsCount = this.rowsCount;
        int newColCount = other.columnsCount;
        ComplexNumber[][] newm = new ComplexNumber[newRowsCount][newColCount];
        for (int i = 0; i < newRowsCount; i++) {
            for (int j = 0; j < newColCount; j++) {
                newm[i][j] = onePositionMult(this.matr,other.matr, this.columnsCount,i,j,0);
            }
        }
        return new matrix(newm,newRowsCount,newColCount);
    }
    public void  transp(){
        ComplexNumber[][] newm = new ComplexNumber [this.columnsCount][this.rowsCount];
        for(int i=0; i<this.rowsCount; i++){
            for(int j=0; j<this.columnsCount; j++){
                newm[j][i] = this.matr[i][j];
            }
        }
        this.matr = newm;
    }
    public void prMatr(){
        for(int i=0; i<this.rowsCount; i++){
            for(int j=0; j<this.columnsCount; j++){
                this.matr[i][j].out("def");
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
    private ComplexNumber[][] defineMinor(ComplexNumber[][] m,int k, int X){
        ComplexNumber[][] m2 = new ComplexNumber[X-1][X-1];
        for(int i=1; i<X; i++){
            int last = 0;
            for(int j=0;j<X;j++){
                if(j!=k){
                    m2[i-1][last] = m[i][j];
                    last+=1;
                }
            }
        }
        return m2;
    }
    private ComplexNumber find(ComplexNumber[][] m, int X) throws IncorrectCountOfSummandsInComplexException,WrongFormatOfSummandsException{
        ComplexNumber result =  new ComplexNumber("0");
        if(X==1){
            result = m[0][0];
        }
        else{
            ComplexNumber z;
            for(int i=0; i<X; i++){
                ComplexNumber[][] minor = defineMinor(m,i,X);
                if(i%2!=0){
                    z = new ComplexNumber("-1");
                }
                else{
                    z = new ComplexNumber("1");
                }
                result = result.substr(m[0][i].multipl(z).multipl(find(minor,X-1)));
            }
        }
        return result;
    }
    public ComplexNumber findDet() throws NotEqualDimensionsOfMatrixException,IncorrectCountOfSummandsInComplexException,WrongFormatOfSummandsException{
        if(this.rowsCount!=this.columnsCount){
            throw new NotEqualDimensionsOfMatrixException("Exception: You have entered a matrix with different dimensions\n");
        }
        ComplexNumber d = find(this.matr,this.rowsCount);
        System.out.print("Determinant:");
        d.out("def");
        return d;
    }
}
