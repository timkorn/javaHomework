package com.metanit;
import java.lang.Math;

public class ComplexNumber {
    private double im;
    private double re;
    public ComplexNumber(String n) throws IncorrectCountOfSummandsInComplexException,WrongFormatOfSummandsException{
        String[] nums =  n.replaceAll(" ","").split("(?=-)|(?=\\+)");
        if(nums.length<1 || nums.length>2){
            throw new IncorrectCountOfSummandsInComplexException("There are" + nums.length + "in this complex number. It is incorrect.");
        }
        if(nums.length == 2){
            try{
                this.re = Integer.parseInt(nums[0]);
            }
            catch (NumberFormatException x){
                throw new WrongFormatOfSummandsException("Real number is not in the right format.\n");
            }
            if("+i".equals(nums[1])){
                this.im = 1;
            }
            else if("-i".equals(nums[1])){
                this.im = -1;
            }
            else{
                char[] x= new char[nums[1].length()-1];
                nums[1].getChars(0, nums[1].length()-1, x, 0);
                String y = new String(x);
                try{
                    this.im = Integer.parseInt(y);
                }
                catch (NumberFormatException f){
                    throw new WrongFormatOfSummandsException("Imaginary number is not in the right format.\n");
                }
            }
        }
        else{
            if(nums[0].indexOf("i")==-1){
                try{
                    this.re = Integer.parseInt(nums[0]);
                }
                catch (NumberFormatException x){
                    throw new WrongFormatOfSummandsException("Real number is not in the right format.\n");
                }
                this.im = 0;
            }
            else{
                if(nums[0] == "i"){
                    this.im = 1;
                }
                else if(nums[0]=="-i"){
                    this.im = -1;
                }
                else{
                    char[] x= new char[nums[1].length()-1];
                    nums[0].getChars(0, nums[1].length()-1, x, 0);
                    String y = new String(x);
                    try{
                        this.im = Integer.parseInt(y);
                    }
                    catch (NumberFormatException f){
                        throw new WrongFormatOfSummandsException("Imaginary number is not in the right format.\n");
                    }
                }
                this.re = 0;
            }
        }
    }
    private ComplexNumber(double re,double im){
        this.re = re;
        this.im = im;
    }
    public ComplexNumber substr(ComplexNumber other){
        double f_re = this.re + other.re;
        double f_im = this.im + other.im;
        ComplexNumber n = new ComplexNumber(f_re,f_im);
        return n;
    }
    public ComplexNumber multipl(ComplexNumber other){
        double f_re = this.re*other.re - this.im*other.im;
        double f_im = this.im*other.re + this.re*other.im;
        ComplexNumber n = new ComplexNumber(f_re,f_im);
        return n;
    }
    public void out(String format){
        ComplConsole(this,format);
    }
    public double prModule(){
        double m = module();
        System.out.print("Module: ");
        System.out.print(m + "\n");
        return m;
    }

    public double module(){
        double m = Math.sqrt(Math.pow(this.re,2) + Math.pow(this.im,2));
        return m;
    }
    public double prArg(){
        double f = arg();
        System.out.print("Argument: ");
        System.out.print(f + "\n");
        return f;
    }
    public double arg(){
        double f = Math.atan(this.im/this.re);
        return f;
    }
    public ComplexNumber division(ComplexNumber other){
        double f_re = (this.re*other.re + this.im*other.im)/(Math.pow(other.re,2) + Math.pow(other.im,2));
        double f_im = (this.im*other.re - this.re*other.im)/(Math.pow(other.re,2) + Math.pow(other.im,2));
        ComplexNumber n = new ComplexNumber(f_re,f_im);
        return n;
    }
    private void ComplConsole(ComplexNumber x,String format){
        if(format == "trigonometric"){
            double r = x.module();
            double f = x.arg();
            System.out.print(r+"*(cos(" + f +") + i*sin("+ f +"))");
        }
        else{
            if(x.im<0){
                double t = x.im*(-1);
                System.out.print(x.re + " - " + t + "i");
            }
            else if(x.im == 0){
                System.out.print(x.re);
            }
            else{
                System.out.print(x.re + " + " + x.im + "i");
            }
        }
    }
}
