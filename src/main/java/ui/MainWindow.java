/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import controllers.ChatController;
import controllers.NetController;
import controllers.UsersController;
import models.*;
import models.implementations.UsersDBImpl;
import models.pojos.Message;
import models.pojos.User;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;


public class MainWindow extends javax.swing.JFrame implements Observer {
    private final Log log;

    public MainWindow(Log log) {
        this.log = log;
        initComponents();
    }

    /**
     * Handles notifications from observed models
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> update(o, arg));
            return;
        }

        if (o instanceof Chat) {
            if (arg == null) {
                log.info("[Window]: Clearing chat box");
                chatTextBox.setText("");
                return;
            }
            Message msg = (Message) arg;
            chatTextBox.append(msg.toString() + "\n");
            log.info("[Window]: Appending msg to the chat box");
            chatTextBox.repaint();
        } else if (o instanceof Log) {
            String msg = (String) arg;
            logTextbox.append(msg);
            logTextbox.repaint();
        } else if (o instanceof UsersDBImpl) {
            String nick = (String) arg;
            log.info("[Window]: Adding new user to the drop-list");
            adresseeComboBox.addItem(nick);
        } else {
            throw new RuntimeException("Unhandled Observable: " + o.toString());
        }

    }

    @SuppressWarnings("unchecked")

    private void initComponents() {
        chatScrollPane = new javax.swing.JScrollPane();
        chatTextBox = new javax.swing.JTextArea();
        chatLabel = new javax.swing.JLabel();
        ipTextInput = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        logTextbox = new javax.swing.JTextArea();
        logLabel = new javax.swing.JLabel();
        messageTextInput = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        connectButton = new javax.swing.JButton();
        adresseeComboBox = new javax.swing.JComboBox<>();
        nickTextbox = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pear2Pear Chat");
        setMinimumSize(new java.awt.Dimension(800, 600));

        chatTextBox.setColumns(20);
        chatTextBox.setRows(5);
        chatScrollPane.setViewportView(chatTextBox);

        chatLabel.setText("Chat with");

        ipTextInput.setText("IP address");

        connectButton.setText("Connect");
        connectButton.addActionListener(this::addUserButtonActionPerformed);

        logTextbox.setColumns(20);
        logTextbox.setRows(5);
        logTextbox.setPreferredSize(new java.awt.Dimension(300, 125));
        logTextbox.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(logTextbox);

        logLabel.setText("Log");

        messageTextInput.setText("Your message here...");

        sendButton.setText("Send");
        sendButton.addActionListener(this::sendButtonActionPerformed);

        adresseeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[0]));

        nickTextbox.setText("Nick");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(chatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(messageTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(logLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(connectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ipTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nickTextbox)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chatLabel)
                        .addGap(18, 18, 18)
                        .addComponent(adresseeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chatLabel)
                    .addComponent(adresseeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ipTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nickTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(connectButton)
                            .addComponent(logLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chatScrollPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(messageTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sendButton))))
                .addContainerGap())
        );

        pack();
    }

    private void addUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectButtonActionPerformed
        new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
                UsersController.addUser(nickTextbox.getText(), ipTextInput.getText());
                return null;
            }
        }.execute();
    }

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {
        final String message = messageTextInput.getText();
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                boolean isSucceeded = NetController.sendMessage((String) adresseeComboBox.getSelectedItem(), message);
                if (isSucceeded) {
                    ChatController.addMessage(User.THIS_USER, message);
                }
                return null;
            }
        }.execute();
        messageTextInput.setText("");
    }

    private javax.swing.JLabel chatLabel;
    private javax.swing.JScrollPane chatScrollPane;
    private javax.swing.JTextArea chatTextBox;
    private javax.swing.JButton connectButton;
    private javax.swing.JTextField ipTextInput;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel logLabel;
    private javax.swing.JTextArea logTextbox;
    private javax.swing.JTextField messageTextInput;
    private javax.swing.JButton sendButton;
    private javax.swing.JComboBox<String> adresseeComboBox;
    private javax.swing.JTextField nickTextbox;
}
