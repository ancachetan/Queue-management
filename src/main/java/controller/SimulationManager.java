package controller;

import model.Server;
import model.Task;
import view.GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable{
    public int numberOfClients;
    public int numberOfServers;
    public int timeLimit;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minProcessingTime;
    public int maxProcessingTime;

    private Scheduler scheduler;
    private GUI frame;
    private List<Task> generatedTasks;

    public SimulationManager(int numberOfClients, int numberOfServers, int timeLimit, int minArrivalTime, int maxArrivalTime, int minProcessingTime, int maxProcessingTime, GUI frame) {
        this.frame = frame;
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;

        this.scheduler = new Scheduler(numberOfServers, numberOfClients);
        this.generatedTasks = new ArrayList<Task>();
        generateRandomTasks();

        for(Server s : this.scheduler.getServers()){
            Thread t = new Thread(s);
            t.start();
        }
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
        for (Server s : scheduler.getServers()){
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

        while (currentTime < timeLimit && generatedTasks.size() > 0 || currentTime < timeLimit && isQueueEmpty() == false){
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

            String result = "";
            result = "Time " + currentTime + "\n" + "Waiting clients: ";
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

            //frame.getTextArea().append(result);
            frame.getTextArea().setText(result);
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
        frame.getTextArea().append(result);

        float avgWaitingTime = 0;
        for (Server s : scheduler.getServers()){
            if (s.getNumberOfClientsInTheQueue().get() > 0) {
                avgWaitingTime += (float) s.getTotalWaitingTime().get() / s.getNumberOfClientsInTheQueue().get();
            }
        }
        avgWaitingTime = avgWaitingTime / numberOfServers;
        frame.getTextArea().append("Average waiting time: " + avgWaitingTime + "\n");

        float avgProcessingTime = 0;
        for (Server s : scheduler.getServers()){
            if (s.getNumberOfClientsInTheQueue().get() > 0) {
                avgProcessingTime += (float) s.getTotalProcessingTime().get() / s.getNumberOfClientsInTheQueue().get();
            }
        }
        avgProcessingTime = avgProcessingTime / numberOfServers;
        frame.getTextArea().append("Average processing time: " + avgProcessingTime + "\n");
        frame.getTextArea().append("Peak hour: " + peakHour + "\n");
    }

    public static void main(String[] args){
        GUI frame = new GUI();
        frame.buttonActionListener();
    }
}
