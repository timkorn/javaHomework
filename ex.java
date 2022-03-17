package com.metanit;


class NoSException extends Exception{
    public NoSException(String message){
        super(message);
    }
}



class IncorrectFormatException  extends Exception{
    public IncorrectFormatException(String message){
        super(message);
    }
}


class WrongDateExeption  extends Exception{
    public WrongDateExeption(String message){
        super(message);
    }
}