package com.p6majo.gui;

import javax.swing.*;

public class TestGui {
    private JTextPane textPane1;
    private JPanel jPanel;
    private JButton button1;
    private JButton button2;
    private JButton button3;

    public static void main(String[] args) {
        JFrame frame = new JFrame("TestGui");
        frame.setContentPane(new TestGui().jPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
