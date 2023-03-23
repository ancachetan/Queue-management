package controller;

import model.Server;
import model.Task;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeStrategy implements Strategy{

    @Override
    public void addTask(List<Server> servers, Task t) {
        int minWaitingPeriod = 9999;
        int pozServer = 0;
        for (int i = 0; i < servers.size(); i++){
            if (servers.get(i).getWaitingPeriod().get() < minWaitingPeriod){
                minWaitingPeriod = servers.get(i).getWaitingPeriod().get();
                pozServer = i;
            }
        }

        for (int i = 0; i < servers.size(); i++){
            if (i == pozServer){
                servers.get(i).addTask(t);
            }
        }

    }
}
