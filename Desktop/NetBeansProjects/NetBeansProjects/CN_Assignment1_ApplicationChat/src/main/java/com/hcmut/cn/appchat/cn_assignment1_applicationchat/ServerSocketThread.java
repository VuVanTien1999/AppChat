/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vu Van Tien
 */
public class ServerSocketThread extends Thread{
    private ServerSocket serverSocket;
    private Socket returnSocket;
    private List<ClientInfo> connectedList;
    
    public ServerSocketThread(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        connectedList = new ArrayList<ClientInfo>();
    }
    
    public void run() {
        System.out.println("Enter run");
        while (true) {
            try {
                returnSocket = serverSocket.accept();
                System.out.println("ServerSocket accept");
                ClientInfo otherInfo = new ClientInfo(returnSocket.getInetAddress().getHostAddress(), returnSocket.getLocalPort());
                
                boolean isContain = false;
                for (int i = 0; i < connectedList.size(); i++) {
                    if (connectedList.get(i).getHost().equals(otherInfo.getHost()) 
                            && connectedList.get(i).getPort() == otherInfo.getPort()) {
                        
                        System.out.println("Receive request" + otherInfo.getPort());
                        isContain = true;
                        
                        break;
                    }
                    System.out.println(connectedList.get(i).getHost()+ "|" + connectedList.get(i).getPort());
                }
                
                if (isContain) continue;
                
                connectedList.add(otherInfo);
                
                System.out.println(returnSocket.getInetAddress().getHostAddress() +"|"+ returnSocket.getLocalPort());
                
                ChatWindowThread chatWindowThread = new ChatWindowThread(
                        serverSocket,
                        returnSocket,
                        new Socket(otherInfo.getHost(), otherInfo.getPort())
                );
                System.out.println("ChatWindowThread pass");
                chatWindowThread.start();

            } catch (IOException ex) {
                Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }

    }

    public Socket getSocket() {
        return returnSocket;
    }
    
    public void setNULL() {
        this.returnSocket = null;
    }
}
