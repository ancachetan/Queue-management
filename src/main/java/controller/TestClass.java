package controller;

import model.Server;
import model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestClass implements Runnable{
    public int numberOfClients;
    public int numberOfServers;
    public int timeLimit;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minProcessingTime;
    public int maxProcessingTime;
    public int flag;

    private Scheduler scheduler;
    private List<Task> generatedTasks;

    public TestClass(int flag) {
        this.flag = flag;
        if (this.flag == 1){
            test1();
        }else if (this.flag == 2){
            test2();
        }else{
            test3();
        }

        this.scheduler = new Scheduler(numberOfServers, numberOfClients);
        this.generatedTasks = new ArrayList<Task>();
        generateRandomTasks();

        for(Server s : this.scheduler.getServers()){
            Thread t = new Thread(s);
            t.start();
        }
    }

    public void test1(){
        this.numberOfClients = 4;
        this.numberOfServers = 2;
        this.timeLimit = 60;
        this.minArrivalTime = 2;
        this.maxArrivalTime = 30;
        this.minProcessingTime = 2;
        this.maxProcessingTime = 4;
    }

    public void test2(){
        this.numberOfClients = 50;
        this.numberOfServers = 5;
        this.timeLimit = 60;
        this.minArrivalTime = 2;
        this.maxArrivalTime = 40;
        this.minProcessingTime = 1;
        this.maxProcessingTime = 7;
    }

    public void test3(){
        this.numberOfClients = 1000;
        this.numberOfServers = 20;
        this.timeLimit = 200;
        this.minArrivalTime = 10;
        this.maxArrivalTime = 100;
        this.minProcessingTime = 3;
        this.maxProcessingTime = 9;
    }

    public void generateRandomTasks(){
        Random rand = new Random();

        for (int i = 0; i < numberOfClients; i++){
            Task t = new Task(rand.nextInt(minArrivalTime, maxArrivalTime), rand.nextInt(minProcessingTime, maxProcessingTime));
            generatedTasks.add(t);
        }

        Collections.sort(generatedTasks);
    }

    public boolean isQueueEmpty(){
        for (Server s: scheduler.getServers()){
            if (s.getTasks().size() > 0){
                return false;
            }
        }

        return true;
    }

    @Override
    public void run() {
        int currentTime = 0;
        int peakHour = 0;
        int maxNoClients = 0;

        BufferedWriter writer = null;
        if (this.flag == 1){
            try {
                writer = new BufferedWriter(new FileWriter("test1.txt"));
            }catch (IOException ex){
                System.out.println("Exception occurred while creating out file");
            }
        }else if(this.flag == 2){
            try {
                writer = new BufferedWriter(new FileWriter("test2.txt"));
            }catch (IOException ex){
                System.out.println("Exception occurred while creating out file");
            }
        }else{
            try {
                writer = new BufferedWriter(new FileWriter("test3.txt"));
            }catch (IOException ex){
                System.out.println("Exception occurred while creating out file");
            }
        }

        while (currentTime < timeLimit && generatedTasks.size() > 0 || currentTime < timeLimit && !isQueueEmpty()){
            int i = 0;

            while (generatedTasks.size() > 0 && i < generatedTasks.size()){
                if (generatedTasks.get(i).getArrivalTime() == currentTime){
                    scheduler.dispatchTask(generatedTasks.get(i));
                    generatedTasks.remove(i);
                }
                else {
                    i++;
                }
            }

            String result = "Time " + currentTime + "\n" + "Waiting clients: ";
            for (i = 0; i < generatedTasks.size(); i++){
                result = result + "(" + generatedTasks.get(i).getIdClient() + ", "+ generatedTasks.get(i).getArrivalTime() + ", " + generatedTasks.get(i).getServiceTime() + ") ";
            }

            result = result + "\n";

            for (i = 0; i < scheduler.getServers().size(); i++){
                if (scheduler.getServers().get(i).getTasks().size() == 0){
                    result = result + "Queue " + (i + 1) + " : " + "closed" + "\n";
                }
                else {
                    result = result + "Queue " + (i + 1)  + " : ";
                    for (Task t : scheduler.getServers().get(i).getTasks()){
                        result = result + "(" + t.getIdClient() + ", " + t.getArrivalTime() + ", " + t.getServiceTime() + ") ";
                    }
                    result = result + "\n";
                }
            }

            try {
                writer.write(result);
            }catch (IOException ex){
                System.out.println("Exception occurred while writing in file");
            }

            currentTime++;

            try {
                Thread.sleep(1000);
            }catch(InterruptedException ex){
                System.out.println("Interrupted!");
                return;
            }

            int noOfTasks = 0;
            for (Server s : scheduler.getServers()){
                noOfTasks += s.getTasks().size();
            }

            if (noOfTasks > maxNoClients){
                maxNoClients = noOfTasks;
                peakHour = currentTime;
            }
        }
        String result = "Time " + currentTime + "\n" + "Waiting clients: \n";
        for (int i = 0; i < numberOfServers; i++){
            result = result + "Queue " + (i + 1) + " : closed\n";
        }

        float avgWaitingTime = 0;
        for (Server s : scheduler.getServers()){
            if (s.getNumberOfClientsInTheQueue().get() > 0) {
                avgWaitingTime += (float) s.getTotalWaitingTime().get() / s.getNumberOfClientsInTheQueue().get();
            }
        }
        avgWaitingTime = avgWaitingTime / numberOfServers;
        result = result + "Average waiting time: " + avgWaitingTime + "\n";

        float avgProcessingTime = 0;
        for (Server s : scheduler.getServers()){
            if (s.getNumberOfClientsInTheQueue().get() > 0) {
                avgProcessingTime += (float) s.getTotalProcessingTime().get() / s.getNumberOfClientsInTheQueue().get();
            }
        }
        avgProcessingTime = avgProcessingTime / numberOfServers;
        result = result + "Average processing time: " + avgProcessingTime + "\n";
        result = result + "Peak hour: " + peakHour + "\n";

        try {
            writer.write(result);
        }catch (IOException ex){
            System.out.println("Exception occurred while writing in file");
        }

        try {
            writer.close();
        }catch (IOException ex){
            System.out.println("Exception occurred while closing the file");
        }
    }

    public static void main(String[] args){
        TestClass test1 = new TestClass(1);
        TestClass test2 = new TestClass(2);
        TestClass test3 = new TestClass(3);

        //Thread t1 = new Thread(test1);
        //Thread t2 = new Thread(test2);
        Thread t3 = new Thread(test3);

        //t1.start();
        //t2.start();
        t3.start();
    }
}
