package com.metanit;

public class Floor {
    public volatile int[] ReqArray= new int[4];
    public volatile String  status = "none";
    public  volatile int number = 0;
    public void add(Request req){
        if(number < 4){
            if(number == 0){
                status = "notEmpty";
            }
            System.out.println("Request created: from: " + req.fromRequest + " to: " + req.toRequest);
            ReqArray[number] = req.toRequest;
            number+=1;
        }
    }
    public void clear(){
        number = 0;
        status = "none";
    }
    public int[]  take(){
        return ReqArray;
    }
}
