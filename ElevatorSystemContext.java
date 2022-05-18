package com.metanit;
import java.util.Arrays;

public class ElevatorSystemContext {
    public Elevators Elevs = new Elevators();
    Object obj = new Object();
    private volatile Floor[] floors;
    private volatile int[] optimalForUp;
    private volatile int[] optimalForDown;
    private volatile int[] optimal;
    private int floorCount = 10;
    public ElevatorSystemContext(){
        floors = new Floor[10];
        optimalForUp = new int[10];
        optimalForDown = new int[10];
        optimal = new int[10];
        for (int i=0; i<10; i++){
            floors[i] = new Floor();
            optimalForUp[i] =-1;
            optimalForDown[i] =-1;
            optimal[i] =-1;
        }

    }
    private void reOptimal(int floor){
        for(int i =0; i<floorCount; i++){
            if(optimal[i] == floor){
                optimal[i] = -1;
            }
            if (optimalForUp[i] == floor){
                optimalForUp[i] = -1;
            }
            if (optimalForDown[i] == floor){
                optimalForDown[i] = -1;
            }
        }
        for(int j=0; j<floorCount; j++){
            if(floors[j].status == "notEmpty"){
                for (int i = 0; i<floorCount; i++){
                    if(j >= i ){
                        if(optimalForUp[i]<j){
                            optimalForUp[i] = j;
                            optimal[i] = j;
                        }
                    }
                    else{
                        if(optimalForDown[i]<=j){
                            optimalForDown[i]=j;
                            if(optimalForUp[i] == -1){
                                optimal[i] = j;
                            }
                        }
                    }
                }
            }
        }

    }

    private void setRequests(Request req) {
        floors[req.fromRequest].add(req);
        makeOptimal(req);
    }
    private void makeOptimal(Request req){
        if(floors[req.fromRequest].number==1){
            for (int i = 0; i<floorCount; i++){
                if(req.fromRequest > i ){
                    if(optimalForUp[i]<req.fromRequest){
                        optimalForUp[i] = req.fromRequest;
                        optimal[i] = req.fromRequest;
                    }
                }
                else{
                    if(optimalForDown[i]<=req.fromRequest){
                        optimalForDown[i]=req.fromRequest;
                        if(optimalForUp[i] == -1){
                            optimal[i] = req.fromRequest;
                        }
                    }
                }
            }
        }
    }

    public Runnable req = () -> {
        int [][] requests = {{6,1},{0,3},{0,5},{8,0},{6,9},{0,2},{0,7}};
        //int [][] requests = {{8,0},{9,0},{4,0}};
        int k =0;
        while(!Thread.currentThread().isInterrupted() && k<requests.length){
            synchronized (obj){
                setRequests(new Request(requests[k][0],requests[k][1]));
            }
            k++;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

            }

        }
    };
    public class Elevators implements Runnable {

        private class State {
            public State(int number){
                this.number = number;
                for(int i=0; i<20; i++){
                    intoElevator[i] = 10000;
                }
            }
            public volatile int floor = 0;
            public volatile int number;
            public volatile String dir = "none";
            public volatile int destination;
            public volatile Floor requests;
            public volatile int[] intoElevator = new int[20];
            private int peopleCount = 0;
            public volatile String status = "empty";

            public void calculateDirection(){
                if (peopleCount==0){
                    dir = "none";
                }
                else{
                    Arrays.sort(intoElevator);
                    int i =0;
                    while ( i<20 && intoElevator[i]<=floor ){
                        i++;
                    }
                    if (i == 0){
                        dir = "up";
                        destination = intoElevator[0];
                        System.out.println("Elevator№ " + number + " has new destination: " + destination);
                    }
                    else{
                        dir = "down";
                        destination = intoElevator[i-1];
                        System.out.println("Elevator№ " + number + " has new destination: " + destination);
                    }
                }
            }
            public void start(int fl){
                System.out.println("Elevator№ " + number + " has new destination: " + fl);
                destination = fl;
                if(destination >= floor){
                    dir ="up";
                }
                else{
                    dir ="down";
                }
            }
            public void add(int fl){
                floors[fl].status="pending";
                reOptimal(fl);
                this.requests = floors[fl];
            }
            public void give(){
                floors[destination].status="none";
            }
            public void clearFloor(){
                int[] buffer =  requests.take().clone();
                int num = requests.number;
                for (int i = 0; i<num; i++){
                    intoElevator[peopleCount] = buffer[i];
                    peopleCount++;
                }
                requests.clear();
                int count = peopleCount;
                for(int i =0; i<count; i++){
                    if(intoElevator[i] == floor){
                        intoElevator[i] = 10000;
                        peopleCount--;
                    }
                }
                if(peopleCount == 0){
                    dir = "none";
                    status = "empty";
                }
                else{
                    Arrays.sort(intoElevator);
                    dir = "wait";
                    status = "notEmpty";
                }
            }
            public void increment(){
                floor+=1;
                System.out.println("Elevator №" + number + "came to floor:" + floor + " destination: " + destination);
            }
            public void decrement(){
                floor-=1;
                System.out.println("Elevator №" + number + "came to floor:" + floor + " destination: " + destination);
            }
        }
        private State elState1 = new State(1);
        private State elState2 = new State(2);

        private class Elevator implements Runnable{
            private State state;
            public Elevator(State state){
                this.state = state;
            }

            public void run(){
                while(!Thread.currentThread().isInterrupted()){
                    if (state.dir == "up"){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {

                        }
                        if(state.floor == state.destination){

                            state.clearFloor();
                        }
                        else{
                            state.increment();
                        }
                    }
                    else if (state.dir == "down"){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {

                        }
                        if(state.floor == state.destination){

                            state.clearFloor();
                        }
                        else{
                            state.decrement();
                        }
                    }
                }
            }
        }

        public void run() {

                Thread elevator1 = new Thread(new Elevator(elState1));
                Thread elevator2 = new Thread(new Elevator(elState2));
                elevator1.start();
                elevator2.start();
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (obj) {
                        if ((elState1.dir == "none") && (elState2.dir == "none")) {
                            int optimal1 = optimal[elState1.floor];
                            int optimal2 = optimal[elState2.floor];
                            if (optimal1 != -1 && optimal2 != -1) {
                                if (optimal1 == optimal2) {
                                    if (Math.abs(optimal1 - elState1.floor) <= Math.abs(optimal1 - elState2.floor)) {
                                        elState1.add(optimal1);
                                        elState1.start(optimal1);
                                        optimal2 = optimal[elState2.floor];
                                        if (optimal[elState2.floor] != -1) {
                                            elState2.add(optimal2);
                                            elState2.start(optimal2);
                                        }
                                    }
                                    else {
                                        elState2.add(optimal2);
                                        elState2.start(optimal2);
                                        if (optimal[elState1.floor] != -1) {
                                            elState2.add(optimal1);
                                            elState2.start(optimal1);
                                        }
                                    }
                                }
                                else{
                                    elState1.add(optimal1);
                                    elState1.start(optimal1);
                                    elState2.add(optimal2);
                                    elState2.start(optimal2);
                                }
                            } else if (optimal1 != -1 && optimal2 != -1) {
                                elState1.add(optimal1);
                                elState1.start(optimal1);
                                elState2.add(optimal2);
                                elState2.start(optimal2);
                            } else if (optimal1 != -1 && optimal2 == -1) {
                                elState1.add(optimal1);
                                elState1.start(optimal1);
                            } else if (optimal1 == -1 && optimal2 != -1) {
                                elState2.add(optimal2);
                                elState2.start(optimal2);
                            }
                        }
                        else if ((elState1.dir == "up") && (elState2.dir == "up")) {
                            int optimal1 = optimalForUp[elState1.floor];
                            int optimal2 = optimalForUp[elState2.floor];
                             if (elState1.destination < optimal1 && elState2.destination < optimal2){
                                if(optimal1 == optimal2){
                                    if (Math.abs(optimal1 - elState1.floor) <= Math.abs(optimal1 - elState2.floor)) {
                                        if (elState1.status == "empty"){
                                            elState1.give();
                                            elState1.add(optimal1);
                                        }
                                        optimal2 = optimalForUp[elState2.floor];
                                        if (optimal2 > elState2.destination  && elState2.status == "empty") {
                                            elState2.give();
                                            elState2.add(optimal2);

                                        }
                                    }
                                    else{
                                        if ( elState2.status == "empty"){
                                            elState2.give();
                                            elState2.add(optimal1);
                                        }
                                        optimal1 = optimalForUp[elState1.floor];
                                        if (optimal1  > elState1.destination && elState1.status == "empty") {
                                            elState1.give();
                                            elState1.add(optimal1);

                                        }
                                    }
                                }
                                else{
                                    if (elState1.status == "empty")
                                    {
                                        elState1.add(optimal1);
                                        elState1.start(optimal1);
                                    }
                                    if (elState1.status == "empty")
                                    {
                                        elState2.add(optimal2);
                                        elState2.start(optimal2);
                                    }
                                }
                             }
                             else if (elState1.destination >= optimal1 && elState2.destination < optimal2) {
                                 if (elState1.status == "empty")
                                 {
                                     elState2.add(optimal2);
                                     elState2.start(optimal2);
                                 }
                             }
                             else if (elState1.destination < optimal1 && elState2.destination >= optimal2) {
                                 if (elState1.status == "empty")
                                 {
                                     elState1.add(optimal1);
                                     elState1.start(optimal1);
                                 }
                             }
                        }
                        else if ((elState1.dir == "down") && (elState2.dir == "down")) {
                            int optimal1 = optimalForDown[elState1.floor];
                            int optimal2 = optimalForDown[elState2.floor];
                            if (elState1.destination < optimal1 && elState2.destination < optimal2){
                                if(optimal1 == optimal2){
                                    if (Math.abs(optimal1 - elState1.floor) <= Math.abs(optimal1 - elState2.floor)) {
                                        elState1.give();
                                        elState1.add(optimal1);
                                        optimal2 = optimalForDown[elState2.floor];
                                        if (optimal2 > elState1.destination) {
                                            elState2.give();
                                            elState2.add(optimal2);
                                        }
                                    }
                                    else{
                                        elState2.give();
                                        elState2.add(optimal2);
                                        optimal1 = optimalForUp[elState1.floor];
                                        if (optimal1 > elState1.destination) {
                                            elState1.give();
                                            elState1.add(optimal1);
                                        }
                                    }
                                }
                                else{
                                    elState1.give();
                                    elState1.add(optimal1);
                                    elState1.start(optimal1);
                                    elState2.give();

                                    elState2.add(optimal2);
                                    elState2.start(optimal2);

                                }
                            }
                            else if (elState1.destination >= optimal1 && elState2.destination < optimal2) {
                                elState1.give();
                                elState2.add(optimal2);
                                elState2.start(optimal2);
                            }
                            else if (elState1.destination < optimal1 && elState2.destination >= optimal2) {
                                elState1.give();
                                elState1.add(optimal1);
                                elState1.start(optimal1);
                            }


                        }
                        else if ((elState1.dir == "up" || elState1.dir == "down") && (elState2.dir == "none")) {
                            int optimal2 = optimal[elState2.floor];
                            if (optimal2!=-1) {
                                elState2.add(optimal2);
                                elState2.start(optimal2);
                            }
                        }
                        else if ((elState1.dir == "none") && (elState2.dir == "up" || elState2.dir=="down")) {
                            int optimal1 = optimal[elState1.floor];
                            if (optimal1!=-1){
                                elState1.add(optimal1);
                                elState1.start(optimal1);
                            }
                        }
                        else if ( ((elState1.dir == "down") && (elState2.dir == "up")) || ((elState2.dir == "down") && (elState1.dir == "up"))) {
                            State Up;
                            State Down;
                            if (elState1.dir == "down"){
                                Down = elState1;
                                Up = elState2;
                            }
                            else{
                                Up = elState1;
                                Down = elState2;
                            }
                            int optimalUp = optimalForUp[Up.floor];
                            int optimalDown = optimalForDown[Down.floor];
                            if (optimalUp > Up.destination && optimalDown >= Down.destination || optimalUp <= Up.destination && optimalDown > Down.destination){
                                    Down.give();
                                    Down.add(optimalDown);
                                    Down.start(optimalDown);
                            }
                            else if(optimalUp > Up.destination && optimalDown < Down.destination){
                                if (Up.status == "empty"){

                                    Up.give();
                                    Up.add(optimalUp);
                                    Up.start(optimalUp);
                                }
                            }
                        }
                        else if((elState1.dir == "wait") || (elState2.dir == "wait")){
                            if(elState1.dir == "wait" && elState2.dir == "wait"){
                                elState1.calculateDirection();
                                elState2.calculateDirection();
                            }
                            else{
                                State waiting;
                                State notWait;
                                if(elState1.dir == "wait"){
                                    waiting=elState1;
                                    notWait = elState2;
                                }
                                else{
                                    waiting=elState2;
                                    notWait = elState1;
                                }
                                waiting.calculateDirection();
                                if (notWait.dir == "none"){
                                    int optimal1 = optimal[notWait.floor];
                                    if (optimal1!=-1){
                                        notWait.add(optimal1);
                                        notWait.start(optimal1);
                                    }
                                }
                                else if (notWait.dir == "up"){
                                    int optimal1 = optimal[notWait.floor];
                                    if (optimal1 > notWait.destination && notWait.status=="empty"){
                                        notWait.give();
                                        notWait.add(optimal1);
                                        notWait.start(optimal1);
                                    }
                                }
                                else if (notWait.dir == "down"){
                                    int optimal1 = optimalForDown[notWait.floor];
                                    if (optimal1 >= notWait.destination){
                                        notWait.give();
                                        notWait.add(optimal1);
                                        notWait.start(optimal1);
                                    }
                                }

                            }

                        }
                    }
                }

        }
    }

}
