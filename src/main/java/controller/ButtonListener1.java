package controller;

import view.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener1 implements ActionListener {
    private GUI frame;

    public ButtonListener1(GUI frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int numberOfClients = Integer.parseInt(frame.getText1().getText());
        int numberOfServers = Integer.parseInt(frame.getText2().getText());
        int timeLimit = Integer.parseInt(frame.getText3().getText());
        int minArrivalTime = Integer.parseInt(frame.getText4().getText());
        int maxArrivalTime = Integer.parseInt(frame.getText5().getText());
        int minProcessingTime = Integer.parseInt(frame.getText6().getText());
        int maxProcessingTime = Integer.parseInt(frame.getText7().getText());

        SimulationManager manager = new SimulationManager(numberOfClients, numberOfServers, timeLimit, minArrivalTime, maxArrivalTime, minProcessingTime, maxProcessingTime, frame);
        Thread t = new Thread(manager);
        t.start();
    }

}
