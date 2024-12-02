package com.example.LapTrinhMang3;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

public class ServerControlPanel extends JFrame {

    private ConfigurableApplicationContext context;
    private JTextArea logArea;

    public ServerControlPanel() {
        setTitle("Server Control Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Log Area
        logArea = new JTextArea(30, 50);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        // Redirect System.out and System.err to JTextArea
        CustomOutputStream customOutputStream = new CustomOutputStream(logArea);
        System.setOut(new PrintStream(customOutputStream));
        System.setErr(new PrintStream(customOutputStream));

        // Control Panel
        JPanel controlPanel = new JPanel();
        JButton startButton = new JButton("Start Server");
        JButton stopButton = new JButton("Stop Server");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (context == null) {
                    log("Starting server...");
                    context = SpringApplication.run(LapTrinhMang3Application.class);
                    log("Server started!");
                } else {
                    log("Server is already running!");
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (context != null) {
                    log("Stopping server...");
                    context.close();
                    context = null;
                    log("Server stopped!");
                } else {
                    log("Server is not running!");
                }
            }
        });

        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerControlPanel controlPanel = new ServerControlPanel();
            controlPanel.setVisible(true);
        });
    }
}
