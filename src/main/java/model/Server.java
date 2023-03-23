package model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private AtomicInteger numberOfClientsInTheQueue;
    private AtomicInteger totalWaitingTime;
    private AtomicInteger totalProcessingTime;

    public Server(int capacity) {
        this.tasks = new ArrayBlockingQueue<Task>(capacity);
        this.waitingPeriod = new AtomicInteger(0);
        this.numberOfClientsInTheQueue = new AtomicInteger(0);
        this.totalWaitingTime = new AtomicInteger(0);
        this.totalProcessingTime = new AtomicInteger(0);
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);

        AtomicInteger newClient = new AtomicInteger(this.getNumberOfClientsInTheQueue().addAndGet(1));
        this.setNumberOfClientsInTheQueue(newClient);

        AtomicInteger totalWaitingTime = new AtomicInteger(this.getTotalWaitingTime().get() + this.getWaitingPeriod().get());
        this.setTotalWaitingTime(totalWaitingTime);

        AtomicInteger totalProcessingTime = new AtomicInteger(this.getTotalProcessingTime().get() + newTask.getServiceTime());
        this.setTotalProcessingTime(totalProcessingTime);

        waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    public AtomicInteger getNumberOfClientsInTheQueue() {
        return numberOfClientsInTheQueue;
    }

    public void setNumberOfClientsInTheQueue(AtomicInteger numberOfClientsInTheQueue) {
        this.numberOfClientsInTheQueue = numberOfClientsInTheQueue;
    }

    public AtomicInteger getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public void setTotalWaitingTime(AtomicInteger totalWaitingTime) {
        this.totalWaitingTime = totalWaitingTime;
    }

    public AtomicInteger getTotalProcessingTime() {
        return totalProcessingTime;
    }

    public void setTotalProcessingTime(AtomicInteger totalProcessingTime) {
        this.totalProcessingTime = totalProcessingTime;
    }

    @Override
    public void run() {
        while (true) {
            if (tasks.size() != 0) {
                Task nextTask = tasks.element();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.out.println("Interrupted!");
                    return;
                }

                waitingPeriod.decrementAndGet();
                nextTask.decrementServiceTime();

                if (nextTask.getServiceTime() <= 0) {
                    tasks.remove(nextTask);
                }
            }
        }
    }
}
