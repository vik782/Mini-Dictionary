/**
 * Vincent Kurniawan
 *
 * GUIServer.java: Server GUI to get information of server processes
 *
 */

package GUI;

import Server.DictionaryThread;
import Server.ThreadPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIServer {
    private JPanel serverPanel;
    private JButton getTotalClients;
    private JLabel showTotalClients;
    private JLabel poolSize;
    private JLabel queueLimit;
    private JButton getPoolSize;
    private JButton getQueueLimit;
    private JButton getLatestRequestButton;
    private JTextArea latestRequest;
    private JButton shutdown;

    public GUIServer (String appName){
        JFrame frame = new JFrame(appName);

        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(serverPanel);
        frame.setVisible(true);

        latestRequest.setLineWrap(true);
        latestRequest.setWrapStyleWord(true);

        getTotalClients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalClients = DictionaryThread.getTotalClients();
                String message = "Total clients currently connected: " + totalClients;
                showTotalClients.setText(message);
            }
        });

        getPoolSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sizePool = ThreadPool.getPoolSize();
                poolSize.setText("Get Pool Size: " + sizePool);
            }
        });


        getQueueLimit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalThreads = ThreadPool.getQueueLimit();
                queueLimit.setText("Get Queue Limit: " + totalThreads);
            }
        });
        getLatestRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String req = DictionaryThread.getLatestRequest();
                latestRequest.setText(req);
            }
        });

        shutdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(
                        null,
                        "Server will shutdown",
                        "Server Shutdown",
                        JOptionPane.DEFAULT_OPTION
                );
                System.exit(1);
            }
        });
    }
}
