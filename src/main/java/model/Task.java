package model;

public class Task implements Comparable{
    private static int ID = 0;
    private int arrivalTime;
    private int serviceTime;
    private int idClient;

    public Task(int arrivalTime, int serviceTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        ID++;
        this.idClient = ID;
    }

    public int getIdClient() {
        return idClient;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public synchronized void decrementServiceTime(){
        this.serviceTime--;
    }

    @Override
    public int compareTo(Object t) {
        if (this.arrivalTime < ((Task)t).getArrivalTime()) {
            return -1;
        }
        else if (this.arrivalTime > ((Task)t).getArrivalTime()){
            return 1;
        }
        else{
            return 0;
        }
    }
}
