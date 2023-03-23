package controller;

import model.Server;
import model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t) {
        int minSize = 9999;
        int pozServer = 0;

        for (int i = 0; i < servers.size(); i++){
            if (servers.get(i).getTasks().size() < minSize){
                minSize = servers.get(i).getTasks().size();
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
