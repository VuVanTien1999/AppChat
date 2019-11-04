/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;

/**
 *
 * @author Vu Van Tien
 */
public class ServerSocketThread extends Thread{
    private ServerSocket serverSocket;
    private Socket returnSocket;
    private List<ChatWindow> chatWindowList;
    private ClientInfo myInfo;
    private ListClientUI listClientUI;
    
    public ServerSocketThread(ListClientUI listClientUI) {
        this.serverSocket = listClientUI.getServerSocket();
        this.myInfo = listClientUI.getMyInfo();
        this.listClientUI = listClientUI;
        this.chatWindowList = new ArrayList<ChatWindow>();
    }
    
    public void run() {
        while (true) {
            try {
                System.out.println("-----> Start ServerSocketThread listening");
                returnSocket = serverSocket.accept();
                
                
                // kiem tra trong connectedList co receivedClientInfo ko? 
                //      hàm updateConnectedList kiểm tra việc này
                // neu khong: new ChatWindowThread moi, them ClientInfo vao list - đã làm trong hàm, 
                //            start ChatWindowThread de new ChatWindow
                //            them ChatWindow vua tao vao listChatWindow
                
                // neu co: tim trong listChatWindow, get ChatWindow ra,
                // new mot receiveThread ung voi ChatWindow da tao de nhan file
                // duoc ben kia co nhu cau gui toi
                
                String receivedUsername = this.getReceivedUsername(returnSocket);
                ClientInfo receivedClientInfo = this.updateConnectedList(receivedUsername);
                
                if (receivedClientInfo == null ) {
                    // CO receivedClientInfo trong connectedList
                    
                    ChatWindow correspondingChatWD = null;
                    for (int i=0; i < this.chatWindowList.size(); i++) {
                        ChatWindow tempChatWD = this.chatWindowList.get(i);
                        if (tempChatWD.getOtherInfo().getUsername().equals(receivedUsername)) {
                            correspondingChatWD = tempChatWD;
                        }
                    }
                    
                    if (correspondingChatWD != null) {
                        ReceiveThread receiveThread = new ReceiveThread(returnSocket, correspondingChatWD);
                        receiveThread.start();
                    }
                }
                else {
                    // KHONG co receivedClientInfo trong connectedList

                    ChatWindow chatWindow = new ChatWindow(
                            serverSocket,
                            returnSocket,
                            receivedClientInfo,
                            myInfo,
                            this.listClientUI
                    );
                    
                    this.addToChatWindowList(chatWindow);
                }

            } catch (IOException ex) {
                Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("----<> End ServerSocketThread listening");
        }

    }

    public Socket getSocket() {
        return returnSocket;
    }
    
    public void setNULL() {
        this.returnSocket = null;
    }
    
    private ChatWindow getChatClient(int i) {
        if (i+1 > chatWindowList.size()) {
            return null;
        }
        else return chatWindowList.get(i);
    }
    
    public List<ClientInfo> getConnectedList() {
        return this.listClientUI.getConnectedList();
    }

    private ClientInfo updateConnectedList(String receivedUsername) {
            ClientInfo receivedClientInfo = null;

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            receivedClientInfo = this.getClientInfoFromOnlineList(receivedUsername);
            // method to get the ClientInfo from listOnlineClient in ListClientUI
            // return null if no ClientInfo specified
            
            
            if (receivedClientInfo==null) {
                System.out.println("Connection is interrupted");
                return null;
            }
            
            boolean isContain = false;
            List<ClientInfo> connectedList;
            connectedList = this.getConnectedList();
            for (ClientInfo clientInfo: connectedList) {
                if (clientInfo.getUsername().equals(receivedUsername)) {
                    isContain = true;
                    break;
                }
            }
            
            if (isContain) {
                return null;
            }
            else {
                this.addToConnectedList(receivedClientInfo);
                return receivedClientInfo;
            }
    }

    public List<ChatWindow> getChatWindowList() {
        return this.chatWindowList;
    }

    private String getReceivedUsername(Socket returnSocket) {
        // return receivedUsername
        // or return String
        
        DataInputStream dataIn = null;
        try {
            dataIn = new DataInputStream(returnSocket.getInputStream());
            String receivedUsername = dataIn.readUTF();
            System.out.println("receivedUsername: " + receivedUsername);
            return receivedUsername;
        } catch (IOException ex) {
            Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
    }

    private ClientInfo getClientInfoFromOnlineList(String receivedUsername) {
        return this.listClientUI.getClientInfoFromOnlineList(receivedUsername);
    }

    private void addToConnectedList(ClientInfo receivedClientInfo) {
        this.getConnectedList().add(receivedClientInfo);
    }

    private void addToChatWindowList(ChatWindow chatWindow) {
        this.getChatWindowList().add(chatWindow);
    }

  
}
