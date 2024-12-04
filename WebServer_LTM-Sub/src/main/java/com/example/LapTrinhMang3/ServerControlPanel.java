package com.example.LapTrinhMang3;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.util.List;

public class ServerControlPanel extends JFrame {

    private ConfigurableApplicationContext context;
    private JTextArea logArea;
    private JTextArea systemInfoArea;
    private JTabbedPane tabbedPane;

    private SystemInfo systemInfo;
    private long[] prevTicks;

    public ServerControlPanel() {
        setTitle("Server Control Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize SystemInfo
        systemInfo = new SystemInfo();

        // Tabbed Pane
        tabbedPane = new JTabbedPane();

        // Log Area Tab
        logArea = new JTextArea(30, 50);
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        tabbedPane.addTab("Log", logScrollPane);

        // System Info Tab
        systemInfoArea = new JTextArea(30, 50);
        systemInfoArea.setEditable(false);
        JScrollPane systemInfoScrollPane = new JScrollPane(systemInfoArea);
        tabbedPane.addTab("System Info", systemInfoScrollPane);

        // Redirect System.out and System.err to JTextArea
        CustomOutputStream customOutputStream = new CustomOutputStream(logArea);
        System.setOut(new PrintStream(customOutputStream));
        System.setErr(new PrintStream(customOutputStream));

        // Control Panel
        JPanel controlPanel = new JPanel();
        JButton startButton = new JButton("Start Server");
        JButton stopButton = new JButton("Stop Server");

        startButton.addActionListener(e -> {
            if (context == null) {
                log("Starting server...");
                context = SpringApplication.run(LapTrinhMang3Application.class);
                log("Server started!");
            } else {
                log("Server is already running!");
            }
        });

        stopButton.addActionListener(e -> {
            if (context != null) {
                log("Stopping server...");
                context.close();
                context = null;
                log("Server stopped!");
            } else {
                log("Server is not running!");
            }
        });

        controlPanel.add(startButton);
        controlPanel.add(stopButton);

        add(controlPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        // Update System Info Periodically
        Timer timer = new Timer(1000, e -> updateSystemInfo());
        timer.start();
    }

    private void updateSystemInfo() {
        HardwareAbstractionLayer hal = systemInfo.getHardware();

        // CPU Info
        CentralProcessor processor = hal.getProcessor();
        if (prevTicks == null) {
            prevTicks = processor.getSystemCpuLoadTicks(); // First-time initialization
        }
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        prevTicks = processor.getSystemCpuLoadTicks(); // Update for next calculation

        // Memory Info
        GlobalMemory memory = hal.getMemory();
        long totalMemory = memory.getTotal() / (1024 * 1024);
        long usedMemory = (memory.getTotal() - memory.getAvailable()) / (1024 * 1024);

        // Network Info
        List<NetworkIF> networkIFs = hal.getNetworkIFs();
        long totalBytesSent = 0, totalBytesReceived = 0;
        for (NetworkIF net : networkIFs) {
            totalBytesSent += net.getBytesSent();
            totalBytesReceived += net.getBytesRecv();
        }

        // Update System Info Area
        StringBuilder sb = new StringBuilder();
        sb.append("CPU Usage: ").append(String.format("%.2f%%", cpuLoad)).append("\n");
        sb.append("Total Memory: ").append(totalMemory).append(" MB\n");
        sb.append("Used Memory: ").append(usedMemory).append(" MB\n");
        sb.append("Total Bytes Sent: ").append(totalBytesSent / (1024 * 1024)).append(" MB\n");
        sb.append("Total Bytes Received: ").append(totalBytesReceived / (1024 * 1024)).append(" MB\n");

        SwingUtilities.invokeLater(() -> systemInfoArea.setText(sb.toString()));
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
