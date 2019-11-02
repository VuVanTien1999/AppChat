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
    private List<ClientInfo> connectedList;
    private List<ChatWindow> chatWindowList;
    private List<ClientInfo> activeList;
    private ClientInfo myInfo;
    
    public ServerSocketThread(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        connectedList = new ArrayList<ClientInfo>();
        chatWindowList = new ArrayList<ChatWindow>();
    }

    ServerSocketThread(ServerSocket serverSocket, List<ClientInfo> connectedList) {
        this(serverSocket);
        this.connectedList = connectedList;
    }

    ServerSocketThread(ServerSocket serverSocket, List<ClientInfo> connectedList, List<ClientInfo> listClient) {
        this(serverSocket);
        this.connectedList = connectedList;
        this.activeList = listClient;
    }

    ServerSocketThread(ServerSocket serverSocket, List<ClientInfo> connectedList, List<ClientInfo> listClient, ClientInfo myInfo) {
        this(serverSocket, connectedList, listClient);
        this.myInfo = myInfo;
    }

    
    
    public void run() {
        while (true) {
            try {
                System.out.println("-----> Start ServerSocketThread listening");
                returnSocket = serverSocket.accept();
                
                this.updateConnectedList(returnSocket); // true neu co update, false neu ko update
                
                // kiem tra trong connectedList co receivedClientInfo ko? 
                //      hàm updateConnectedList kiểm tra việc này
                // neu khong: new ChatWindowThread moi, them ClientInfo vao list - đã làm trong hàm, 
                //            start ChatWindowThread de new ChatWindow
                //            them ChatWindow vua tao vao listChatWindow
                
                // neu co: tim trong listChatWindow, get ChatWindow ra,
                // new mot receiveThread ung voi ChatWindow da tao de nhan file
                // duoc ben kia co nhu cau gui toi
                
                ClientInfo receivedClientInfo = this.updateConnectedList(returnSocket);
                if (receivedClientInfo == null ) {
                    // CO receivedClientInfo trong connectedList
                    
//                    for (ChatWindow chatWindow: chatWindowList){
//                        if (chatWindow.getMyInfo().getUsername().equals(receivedClientInfo.getUsername())) {
//                            correspondingChatWD = chatWindow;
//                        }
//                    }

                    ChatWindow correspondingChatWD = null;
                    for (int i=0; i<chatWindowList.size(); i++) {
                        ChatWindow tempChatWD = chatWindowList.get(i);
                        
                        if (tempChatWD.getMyInfo().getUsername().equals(receivedClientInfo.getUsername())) {
                            correspondingChatWD = tempChatWD;
                        }
                    }
                    
                    if (correspondingChatWD != null) {
                        ReceiveThread receiveThread = new ReceiveThread(serverSocket, correspondingChatWD);
                        receiveThread.start();
                    }
                }
                else {
                    // KHONG co receivedClientInfo trong connectedList
                    ChatWindowThread chatWindowThread = new ChatWindowThread(
                            serverSocket, 
                            returnSocket, 
                            receivedClientInfo, 
                            myInfo
                    );
                    chatWindowThread.start();
                    ChatWindow chatWindow = chatWindowThread.getChatWindow();
                    
                    chatWindowList.add(chatWindow);
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

    public void setConnectedList(List<ClientInfo> connectedList) {
        this.connectedList = connectedList;
    }
    
    public List<ClientInfo> getConnectedList() {
        return this.connectedList;
    }

    private ClientInfo updateConnectedList(Socket returnSocket) {
        try {
            DataInputStream dataIn = new DataInputStream(returnSocket.getInputStream());
            String receivedUsername = dataIn.readUTF();
            System.out.println("receivedUsername: " + receivedUsername);
            ClientInfo receivedClientInfo = null;
            
//            for (ClientInfo clientInfo: activeList) {
//                if (clientInfo.getUsername().equals(receivedUsername)) {
//                    receivedClientInfo = clientInfo;
//                }
//                System.out.println("username in activeList: " + clientInfo.getUsername() + "|");
//            }

            for (int i=0; i<activeList.size(); i++) {
                ClientInfo tempInfo = activeList.get(i);
                if (tempInfo.getUsername().equals(receivedUsername)) {
                    receivedClientInfo = tempInfo;
                }
            }
            
            if (receivedClientInfo==null) {
                System.out.println("Connection is interrupted");
                return null;
            }
            
            boolean isContain = false;
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
                connectedList.add(receivedClientInfo);
                return receivedClientInfo;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
