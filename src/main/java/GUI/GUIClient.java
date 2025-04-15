/**
 * Vincent Kurniawan
 *
 * GUIClient.java: client GUI to interact with server
 *
 */

package GUI;

import Client.Middleware;
import Response.ResponseCodes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUIClient {

    private JPanel clientPanel;

    private JButton searchButton;
    private JButton removeButton;
    private JButton addButton;
    private JButton updateButton;
    private JTextField queryText;

    private JTextArea queryResult;
    private JLabel updateLabel;
    private JLabel removeLabel;
    private JLabel addLabel;
    private JLabel searchLabel;
    private JLabel textFieldLabel;

    private Middleware clientAPI;

    private JFrame mainFrame;

    public GUIClient (String appName, Middleware clientAPI){
        JFrame frame = new JFrame(appName);
        mainFrame = frame;
        this.clientAPI = clientAPI;
        queryResult.setLineWrap(true);
        queryResult.setWrapStyleWord(true);
        queryResult.setEditable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.setSize(500, 400);
        frame.add(clientPanel);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchReq = queryText.getText();
                try {
                    clientAPI.checkSearchRequest(searchReq, queryResult);
                } catch (IOException err) {
                    throw new RuntimeException(err);
                }
            }
        });


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String addReq = queryText.getText();
                Boolean check;
                try {
                    check = clientAPI.validateAddRequest(addReq, queryResult);
                } catch (IOException err) {
                    throw new RuntimeException(err);
                }
                if (check){
                    GUIClient parent = GUIClient.this;
                    frame.setEnabled(false);
                    frame.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
                    AddUpdateDialog popUp = new AddUpdateDialog(parent, addReq, ResponseCodes.ADD_QUERY);
                }

            }

        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String updateReq = queryText.getText();
                Boolean check;
                try {
                    check = clientAPI.validateUpdateRequest(updateReq, queryResult);
                } catch (IOException err) {
                    throw new RuntimeException(err);
                }
                if (check){
                    GUIClient parent = GUIClient.this;
                    frame.setEnabled(false);
                    frame.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
                    AddUpdateDialog popUp = new AddUpdateDialog(parent, updateReq, ResponseCodes.UPDATE_QUERY);
                }

            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String removeReq = queryText.getText();
                // String[] req = (Response.Response.ResponseCodes.SEARCH_QUERY + " " + queryText.getText()).split(" ");
                try {
                    clientAPI.checkRemoveRequest(removeReq, queryResult);
                } catch (IOException err) {
                    throw new RuntimeException(err);
                }

            }
        });
    }

    public Middleware getClientAPI(){
        return clientAPI;
    }

    public JFrame getFrame(){
        return mainFrame;
    }

    public JTextArea getQueryResult(){
        return queryResult;
    }
}
