package view;
import controller.ButtonListener1;

import java.awt.*;
import javax.swing.*;

public class GUI extends JFrame{
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JTextField text1;
    private JTextField text2;
    private JTextField text3;
    private JTextField text4;
    private JTextField text5;
    private JTextField text6;
    private JTextField text7;
    private JTextArea textArea;
    private JButton button;

    public GUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(650, 650);
        this.setTitle("Queues management application");

        this.label1 = new JLabel("Number of clients:   ");
        this.label2 = new JLabel("Number of queues:    ");
        this.label3 = new JLabel("Simulation interval: ");
        this.label4 = new JLabel("Min arrival time: ");
        this.label5 = new JLabel("Max arrival time: ");
        this.label6 = new JLabel("Min service time: ");
        this.label7 = new JLabel("Max service time: ");

        this.text1 = new JTextField(10);
        this.text2 = new JTextField(10);
        this.text3 = new JTextField(10);
        this.text4 = new JTextField(10);
        this.text5 = new JTextField(10);
        this.text6 = new JTextField(10);
        this.text7 = new JTextField(10);

        this.textArea = new JTextArea(20,60);
        JScrollPane scroll = new JScrollPane(this.textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.button = new JButton("START SIMULATION");

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panel5 = new JPanel();
        JPanel panel6 = new JPanel();
        JPanel panel7 = new JPanel();
        JPanel p = new JPanel();

        panel1.setLayout(new FlowLayout());
        panel1.add(label1);
        panel1.add(text1);

        panel2.setLayout(new FlowLayout());
        panel2.add(label2);
        panel2.add(text2);

        panel3.setLayout(new FlowLayout());
        panel3.add(label3);
        panel3.add(text3);

        panel4.setLayout(new FlowLayout());
        panel4.add(label4);
        panel4.add(text4);
        panel4.add(label5);
        panel4.add(text5);

        panel5.setLayout(new FlowLayout());
        panel5.add(label6);
        panel5.add(text6);
        panel5.add(label7);
        panel5.add(text7);

        panel6.setLayout(new FlowLayout());
        panel6.add(button);

        panel7.setLayout(new FlowLayout());
        panel7.add(scroll);

        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(panel1);
        p.add(panel2);
        p.add(panel3);
        p.add(panel4);
        p.add(panel5);
        p.add(panel6);
        p.add(panel7);

        this.setContentPane(p);
        this.setVisible(true);
    }

    public JTextField getText1() {
        return text1;
    }

    public JTextField getText2() {
        return text2;
    }

    public JTextField getText3() {
        return text3;
    }

    public JTextField getText4() {
        return text4;
    }

    public JTextField getText5() {
        return text5;
    }

    public JTextField getText6() {
        return text6;
    }

    public JTextField getText7() {
        return text7;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void buttonActionListener(){
        ButtonListener1 listener1 = new ButtonListener1(this);
        this.button.addActionListener(listener1);
    }

}
