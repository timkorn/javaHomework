package com.metanit;

public class elevatorSystem {
    Thread requestsSystem;
    Thread twoElevators;
    ElevatorSystemContext Context = new ElevatorSystemContext();

    public void start(){
        requestsSystem = new Thread(Context.req);
        twoElevators = new Thread(Context.Elevs);
        requestsSystem.start();
        twoElevators.start();
    }
}
