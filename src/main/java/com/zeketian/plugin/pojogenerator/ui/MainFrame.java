package com.zeketian.plugin.pojogenerator.ui;

import lombok.Getter;

import javax.swing.*;

/**
 * @author zeke
 * @description 主界面的内容
 * @date created in 2022/10/5 23:30
 */
@Getter
public class MainFrame extends JFrame {

    private JPanel mainPanel;

    private JTextField inputPackage;

    private JTextArea inputSql;

    public MainFrame() {
        setSize(600, 300);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        MainFrame mainFrame = new MainFrame();
        frame.setContentPane(mainFrame.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
