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
    private List<ClientInfo> onlineFriendList;
    private ClientInfo myInfo;
    private ListClientUI listClientUI;
    
    public ServerSocketThread(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        connectedList = new ArrayList<ClientInfo>();
        chatWindowList = new ArrayList<ChatWindow>();
    }

    ServerSocketThread(ServerSocket serverSocket, List<ClientInfo> connectedList) {
        this(serverSocket);
        this.connectedList = connectedList;
    }

    ServerSocketThread(ServerSocket serverSocket, List<ClientInfo> connectedList, List<ClientInfo> onlineFriendList) {
        this(serverSocket);
        this.connectedList = connectedList;
        this.onlineFriendList = onlineFriendList;
    }

    ServerSocketThread(ServerSocket serverSocket, List<ClientInfo> connectedList, List<ClientInfo> onlineFriendList, ClientInfo myInfo) {
        this(serverSocket, connectedList, onlineFriendList);
        this.myInfo = myInfo;
    }

    ServerSocketThread(ServerSocket serverSocket, List<ClientInfo> listConnectedClient, List<ClientInfo> listOnlineClient, ClientInfo myInfo, ListClientUI listClientUI) {
        this(serverSocket, listConnectedClient, listOnlineClient, myInfo);
        this.listClientUI = listClientUI;
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
                    for (int i=0; i<this.getChatWindowList().size(); i++) {
                        ChatWindow tempChatWD = this.getChatWindowList().get(i);
                        System.out.println("In SVSKThread, getfromOtherInfo: " + tempChatWD.getOtherInfo().getUsername());
                        System.out.println("In SVSKThread, receivedUsername: " + receivedUsername);
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
//                    ChatWindowThread chatWindowThread = new ChatWindowThread(
//                            serverSocket, 
//                            returnSocket, 
//                            receivedClientInfo, 
//                            myInfo
//                    );
//                    chatWindowThread.start();
//                    ChatWindow chatWindow = chatWindowThread.getChatWindow();
//                    
//                    chatWindowList.add(chatWindow);

                    ChatWindow chatWindow = new ChatWindow(
                            serverSocket,
                            returnSocket,
                            receivedClientInfo,
                            myInfo,
                            this.listClientUI
                    );

                    chatWindowList.add(chatWindow);
                    
                    //join
                    // wait for ChatWindowThread executed to continue
                    // 
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

    private ClientInfo updateConnectedList(String receivedUsername) {

            ClientInfo receivedClientInfo = null;


            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            receivedClientInfo = this.listClientUI.getClientInfoInOnlineList(receivedUsername);
            // method to get the ClientInfo from listOnlineClient in ListClientUI
            // return null if no ClientInfo specified
            
            
            
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
            

    }

    public List<ChatWindow> getChatWindowList() {
        return chatWindowList;
    }

    private String getReceivedUsername(Socket returnSocket) {
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

  
}
