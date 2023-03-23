package controller;

import model.Server;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int numberOfTasksPerServer;
    private int numberOfServers;
    private Strategy strategy;

    public Scheduler(int numberOfServers, int numberOfTasksPerServer) {
        this.numberOfTasksPerServer = numberOfTasksPerServer;
        this.numberOfServers = numberOfServers;
        this.servers = new ArrayList<Server>();
        for (int i = 0; i < numberOfServers; i++){
            Server server = new Server(numberOfTasksPerServer);
            this.servers.add(server);
        }
        this.strategy = new TimeStrategy();
    }

    public void dispatchTask(Task t){
        this.strategy.addTask(servers, t);
    }

    public List<Server> getServers() {
        return servers;
    }
}
