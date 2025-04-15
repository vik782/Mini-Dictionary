/**
 * Vincent Kurniawan
 *
 * AddUpdateDialog.java: pop-up GUI for add and update operations by client
 *
 */

package GUI;

import Response.ErrorExceptions;
import Response.ResponseCodes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class AddUpdateDialog extends JFrame {
    private JTextArea meaningTextArea;
    private JLabel keyword;
    private JLabel keywordLabel;
    private JLabel meaning;
    private JButton enterButton;
    private JTextField addMeaning;
    private JButton addButton;

    private JPanel popUpPanel;
    private JLabel meaningWarning;
    private JLabel warningAll;

    private String addOrUpdate;

    private int counter = 0;

    public AddUpdateDialog (GUIClient parent, String keyword, String addOrUpdate){
        this.addOrUpdate = addOrUpdate;
        setTitle("Pop Up Dialog");
        setSize(600, 400);
        add(popUpPanel);
        setVisible(true);

        keywordLabel.setText(keyword);
        ArrayList<String> meanings = new ArrayList<String>();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String meaning = addMeaning.getText();
                if (!meaning.isEmpty() && meaning != null) {
                    // add meaning
                    counter++;
                    String added = counter + ". " + meaning + "\n";
                    meanings.add(added);
                    meaningTextArea.append(added);
                    addMeaning.setText("");
                }
                else{
                    // meaning is empty warning
                    ErrorExceptions.emptyInput();
                }
            }
        });

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputted = meaningTextArea.getText();

                if ((inputted == null) || (inputted.isEmpty())) {
                    // a word cannot have no meaning warning
                    ErrorExceptions.emptyInput();
                } else {
                    // add all associated meanings to the keyword
                    int confirmation = JOptionPane.showConfirmDialog(
                            null,
                            inputted,
                            "Submit: " + keywordLabel.getText(),
                            JOptionPane.OK_CANCEL_OPTION
                    );

                    switch (confirmation) {
                        case JOptionPane.YES_OPTION:
                            try {
                                // client press OK, proceed to add meanings
                                parent.getClientAPI().checkAddRequest(keyword, meaningTextArea.getText(), addOrUpdate, parent.getQueryResult() );
                            } catch (IOException err) {
                                ErrorExceptions.connectRefusedError(err);
                            }
                            meanings.clear();
                            meaningTextArea.setText("");
                            parent.getFrame().setEnabled(true);
                            dispose();
                            break;

                            // client cancels in adding meanings, abort operation
                        case JOptionPane.CANCEL_OPTION:
                        case JOptionPane.CLOSED_OPTION:
                            meanings.clear();
                            meaningTextArea.setText("");
                            parent.getFrame().setEnabled(true);
                            dispose();
                    }
                }
            }
        });
        // enables back the client GUI (parent frame)
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                parent.getFrame().setEnabled(true);
            }
        });
    }
}
