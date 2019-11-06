/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import javax.swing.Timer;

/**
 *
 * @author Van Tien Vu & Huy Tran & Viet Hoang Nguyen
 */
public class ListClientUI extends javax.swing.JFrame {
    private static ChatClient chatClient;
    private ClientInfo myInfo;
    
    private ServerSocket serverSocket;
    private ServerSocketThread serverSocketThread;
    
    private List<ClientInfo> listClient;
    private List<ClientInfo> listOnlineClient;
    private List<ClientInfo> listConnectedClient;
    
    private List<String> listFriendRequest = new ArrayList<>();

    private AddFriendUI addFriend;
    
    /**
     * Creates new form ListClientUI
     */
    public ListClientUI(ChatClient chatClient) {
        // Initialize ListClientUI
        initComponents();
        this.setVisible(true);
        
        // Get client list from ChatClient
        this.chatClient = chatClient;
        this.listClient = this.chatClient.getClientList();
        this.listOnlineClient = this.listClient.stream().filter(client -> client.getActiveStatus()).collect(Collectors.toList());
        this.listConnectedClient = new ArrayList<ClientInfo>();
        this.myInfo = chatClient.getMyInfo();
        
        // Set ListClientUI to the center of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getSize().width/2, dim.height/2 - this.getSize().height/2);
        
        // Set display mode of three lists in ListClientUI
        onlineUserList.setEnabled(true);
        offlineUserList.setEnabled(false);
        friendRequestList.setEnabled(true);
        
        // Disable unnecessary buttons
        this.btnAceptFriendRequest.setEnabled(false);
        this.btnDeclineFriendRequest.setEnabled(false);
        
        // Create ServerSocket for client to listen for other clients
        try {
            this.serverSocket = new ServerSocket(chatClient.getMyPort());
        } catch (IOException ex) {
            Logger.getLogger(ListClientUI.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error when creating ServerSocket");
            ex.printStackTrace();
        }
        
        // Pass ServerSocket & connected client list to another thread then start itself
        this.serverSocketThread = new ServerSocketThread(
                this
        );
        this.serverSocketThread.start();

        // Create models for three lists above
        DefaultListModel modelForOnlineUsers = new DefaultListModel();
        DefaultListModel modelForOfflineUsers = new DefaultListModel();
        DefaultListModel modelForFriendRequests = new DefaultListModel();
        
        // Set models for these lists
        onlineUserList.setModel(modelForOnlineUsers);
        offlineUserList.setModel(modelForOfflineUsers);
        friendRequestList.setModel(modelForFriendRequests);
        
        // Create another thread for refreshing these lists
        Timer refreshList = new Timer(2000, (ActionEvent e) -> {
            modelForOnlineUsers.removeAllElements();
            modelForOfflineUsers.removeAllElements();
            //modelForFriendRequests.removeAllElements();
                                   
            listClient = chatClient.getClientList();
            listOnlineClient = listClient.stream()
                    .filter(client -> client.getActiveStatus())
                    .collect(Collectors.toList());

            System.out.println("Time refreshList, listClient: " + listClient.size());
            System.out.println("Time refreshList, listOnlineClient size: " + listOnlineClient.size());
            System.out.println("Time refreshList, listConnectedClient size: " + listConnectedClient.size());
            System.out.println("Time refreshList, listChatWindow size: " + this.getChatWindowList().size());

            listClient.forEach((client) -> {
                if (client.getActiveStatus()) {
                    modelForOnlineUsers.addElement(client.getDisplayedName() + "(" + client.getUsername() + ")");
                }
                else {
                    modelForOfflineUsers.addElement(client.getDisplayedName() + "(" + client.getUsername() + ")");
                }
            });
            
            List<String> tempListFriendRequest = chatClient.getFriendRequest();
            if (tempListFriendRequest.size() > listFriendRequest.size()) {
                for (int i = listFriendRequest.size(); i < tempListFriendRequest.size(); i++) {
                    listFriendRequest.add(i, tempListFriendRequest.get(i));
                    modelForFriendRequests.addElement(tempListFriendRequest.get(i));
                }
            }  
        });
        refreshList.start();
    }

    public void listen(int port) {
        ServerSocketThread serverSocketThread;
        ServerSocket serverSocket;
        
        try {
            serverSocket = new ServerSocket(port);
            serverSocketThread = new ServerSocketThread(this);
            serverSocketThread.start();

            Socket socketReturn;
            while (true) {
                if (serverSocketThread.getSocket() != null) {
                    socketReturn = serverSocketThread.getSocket();
                    serverSocketThread.setNULL();
                    ClientInfo otherClient = new ClientInfo(socketReturn.getInetAddress().getHostAddress(), socketReturn.getPort());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ListClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean isContainInList(ClientInfo otherInfo) {
        boolean isContain = false;
        for (int i = 0; i < listConnectedClient.size(); i++) {
            if (listConnectedClient.get(i).getHost().equals(otherInfo.getHost())
                    && listConnectedClient.get(i).getPort() == otherInfo.getPort()) {

                System.out.println("Receive request from: " + otherInfo.getPort());
                isContain = true;

                break;
            }
            System.out.println(listConnectedClient.get(i).getHost() + "|241|" + listConnectedClient.get(i).getPort());
        }
        
        // print array
        for (int i=0; i<listConnectedClient.size(); i++) {
            System.out.println(listConnectedClient.get(i).getPort());
        }
        System.out.println("other port: " + otherInfo.getPort());

        return isContain;
    }

    private void printConnectedList() {
        System.out.println("Connected list:");
        for (int i=0; i<listConnectedClient.size(); i++) {
            System.out.print(listConnectedClient.get(i).getDisplayedName());
            if (i<listConnectedClient.size()-1) {
                System.out.print(", ");
            }
            else System.out.println();
        }
    }
    
    private void sendUsernameToOther(Socket socketToOther) {
        try {
            DataOutputStream clientOut = new DataOutputStream(socketToOther.getOutputStream());
            System.out.println(myInfo.getUsername());
            clientOut.writeUTF(myInfo.getUsername());
            
        } catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        onlineUserList = new javax.swing.JList<>();
        onlineUsersLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        offlineUserList = new javax.swing.JList<>();
        offlineUsersLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        friendRequestList = new javax.swing.JList<>();
        friendRequestsLabel = new javax.swing.JLabel();
        btnAceptFriendRequest = new javax.swing.JButton();
        btnDeclineFriendRequest = new javax.swing.JButton();
        btnAddFriend = new javax.swing.JButton();

        jToggleButton1.setText("jToggleButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        onlineUserList.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        onlineUserList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        onlineUserList.setVisibleRowCount(-1);
        onlineUserList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onlineUserListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(onlineUserList);

        onlineUsersLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        onlineUsersLabel.setText("Pick someone down there to start chat");
        onlineUsersLabel.setPreferredSize(new java.awt.Dimension(160, 40));

        offlineUserList.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        offlineUserList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane2.setViewportView(offlineUserList);

        offlineUsersLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        offlineUsersLabel.setText("Oops, people down here are not available");
        offlineUsersLabel.setPreferredSize(new java.awt.Dimension(250, 40));

        friendRequestList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        friendRequestList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                friendRequestListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(friendRequestList);

        friendRequestsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        friendRequestsLabel.setText("Friend requests");

        btnAceptFriendRequest.setText("Accept");
        btnAceptFriendRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptFriendRequestActionPerformed(evt);
            }
        });

        btnDeclineFriendRequest.setText("Decline");
        btnDeclineFriendRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeclineFriendRequestActionPerformed(evt);
            }
        });

        btnAddFriend.setText("Let's make a friend");
        btnAddFriend.setActionCommand("");
        btnAddFriend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFriendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(onlineUsersLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(offlineUsersLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnDeclineFriendRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnAceptFriendRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(friendRequestsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(25, 25, 25)
                            .addComponent(btnAddFriend, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(onlineUsersLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(offlineUsersLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(friendRequestsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(btnAddFriend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(btnAceptFriendRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(btnDeclineFriendRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onlineUserListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onlineUserListMouseClicked
        System.out.println("---Click Mouse on List------------------------");
        if (onlineUserList.getSelectedIndex() != -1) {
            ClientInfo otherInfo = this.listOnlineClient.get(onlineUserList.getSelectedIndex());

            System.out.println("Click on: " + otherInfo.getDisplayedName());
            this.printConnectedList();
                        
            if (this.isContainInList(otherInfo)) {
                System.out.println("Selected Client is in connect");
                System.out.println("---------------------------");
                return;
            }
            
            listConnectedClient.add(otherInfo);
            
            try {
                Socket socketToOther = new Socket(otherInfo.getHost(), otherInfo.getPort());
                
                // Send my username to client who is being clicked
                this.sendUsernameToOther(socketToOther);

                ChatWindow chatWindow = new ChatWindow(
                        this.serverSocket,
                        socketToOther,
                        otherInfo,
                        this.myInfo,
                        this
                );
                this.addToChatWindowList(chatWindow);
                
                // After connect success, check if connection interrupted
                // Remove B from connectList, B's chatWindow from chatWindowList
                // Use join to wait for ChatWindowThread is executed

            } catch (IOException ex) {
                Logger.getLogger(ListClientUI.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
            
        }
        // UI clear selected index
        onlineUserList.clearSelection();
        System.out.println("---End Click Mouse on List------------------------");
    }//GEN-LAST:event_onlineUserListMouseClicked

    private void friendRequestListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_friendRequestListMouseClicked
        if (friendRequestList.getSelectedIndex() != -1) {
            this.btnAceptFriendRequest.setEnabled(true);
            this.btnDeclineFriendRequest.setEnabled(true);
        }
    }//GEN-LAST:event_friendRequestListMouseClicked

    private void btnAceptFriendRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptFriendRequestActionPerformed
        int selectedIndex = this.friendRequestList.getSelectedIndex();
        String usernameRequest = "";
        
        if (selectedIndex != -1) {
            usernameRequest = this.friendRequestList.getSelectedValue();
            System.out.println(usernameRequest);
            chatClient.acceptFriendRequest(usernameRequest);
        }
        
        this.friendRequestList.clearSelection();
        //this.listFriendRequest.remove(usernameRequest);
        this.friendRequestList.remove(selectedIndex);
        this.btnAceptFriendRequest.setEnabled(false);
        this.btnDeclineFriendRequest.setEnabled(false);
    }//GEN-LAST:event_btnAceptFriendRequestActionPerformed

    private void btnDeclineFriendRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeclineFriendRequestActionPerformed
        // TODO add your handling code here:
        int selectedIndex = this.friendRequestList.getSelectedIndex();
        if (selectedIndex != -1) {
            String usernameRequest = this.friendRequestList.getSelectedValue();
            
            System.out.println("Click on: " + usernameRequest);
            
            chatClient.declineFriendRequest(usernameRequest);

            System.out.println("Remove friend reuqest: " + usernameRequest);            
            
            this.friendRequestList.remove(selectedIndex);
        }
        
        this.friendRequestList.clearSelection();
        this.btnAceptFriendRequest.setEnabled(false);
        this.btnDeclineFriendRequest.setEnabled(false);
    }//GEN-LAST:event_btnDeclineFriendRequestActionPerformed

    private void btnAddFriendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFriendActionPerformed
        // TODO add your handling code here:
        this.addFriend = new AddFriendUI();
        
        Thread getFriendRequest = new Thread() {
            public void run() {
                while(true) {
                    synchronized(addFriend) {
                        try {
                            addFriend.wait();
                        } catch(InterruptedException ez){
                            ez.printStackTrace();
                        }
                    }

                    if (addFriend.isSend()) {
                        String result = chatClient.sendFriendRequest(addFriend.getUsername());

                        addFriend.setLabel(result);
                    }
                    else {
                        addFriend.setVisible(false);
                        break;
                    }
                }
            }
        };     
        getFriendRequest.start();
    }//GEN-LAST:event_btnAddFriendActionPerformed
            
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ListClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListClientUI(chatClient);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptFriendRequest;
    private javax.swing.JButton btnAddFriend;
    private javax.swing.JButton btnDeclineFriendRequest;
    private javax.swing.JList<String> friendRequestList;
    private javax.swing.JLabel friendRequestsLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JList<String> offlineUserList;
    private javax.swing.JLabel offlineUsersLabel;
    private javax.swing.JList<String> onlineUserList;
    private javax.swing.JLabel onlineUsersLabel;
    // End of variables declaration//GEN-END:variables

    public ClientInfo getClientInfoFromOnlineList(String receivedUsername) {
        // method to get the ClientInfo from listOnlineClient in ListClientUI
        // return null if there is no ClientInfo specified
        
        for (int i=0; i<listOnlineClient.size(); i++) {
            ClientInfo tempClientInfo = listOnlineClient.get(i);
            
            if (tempClientInfo.getUsername().equals(receivedUsername)) {
                return tempClientInfo;
            }
        }
        return null;
    }
    
    public List<ClientInfo> getConnectedList() {
        System.out.println("In ListClientUI, listConenctedClient size: " + listConnectedClient.size());
        return this.listConnectedClient;
    }

    public List<ChatWindow> getChatWindowList() {
        return this.serverSocketThread.getChatWindowList();
    }

    ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    ClientInfo getMyInfo() {
        return this.myInfo;
    }

    private void addToChatWindowList(ChatWindow chatWindow) {
        this.getChatWindowList().add(chatWindow);
    }
}
