/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hcmut.cn.appchatserver.cn_assignment1_applicationchatserver;

import java.io.*;
import java.net.*;
import java.util.*;
import javafx.util.*;

/**
 *
 * @author nguye
 */

public class UserThread extends Thread {
    private Socket serverSocket;
    private ChatServer server;
    private String usernameOwningThread;
    
    private DataInputStream toServer;
    private DataOutputStream fromServer;
        
    public UserThread(ChatServer server, Socket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
    }
    
    public void run() {
        try {
            toServer = new DataInputStream(serverSocket.getInputStream());
            fromServer = new DataOutputStream(serverSocket.getOutputStream());
            
            // Create or Verify account sent from client
            do {
                String temp = toServer.readUTF(); 
                
                if (temp.equals("Create account")) {
                    String username, password, displayedName;
                
                    username = toServer.readUTF();
                    password = toServer.readUTF();
                    displayedName = toServer.readUTF();

                    System.out.print(System.getProperty("line.separator") + "Create: " + username + " " + password + " " + displayedName);

                    //Write acocunt info to file
                    if (server.isAccountExisted(username, password, displayedName)) {
                        fromServer.writeUTF("false");
                    }
                    else {
                        fromServer.writeUTF("true");
                    }
                }
                else if (temp.equals("Verify account")) {
                    String username, password, host;
                    int port;
                
                    username = toServer.readUTF();
                    password = toServer.readUTF();
                    host = toServer.readUTF();
                    port = Integer.valueOf(toServer.readUTF());

                    System.out.print(System.getProperty("line.separator") + "Verify: " + username + " " + password);

                    if (server.isAccountValid(username, password, host, port)) {
                        this.usernameOwningThread = username;
                        fromServer.writeUTF("true");
                        break;
                    }
                    else fromServer.writeUTF("false");
                }
            } while(true);
            
            while(true) {
                List<AccountProfile> listActiveUser = this.server.getUserProfile();
                fromServer.writeUTF(String.valueOf(listActiveUser.size()));    

                for (int i = 0 ; i < listActiveUser.size(); i++) {
                    AccountProfile temp = listActiveUser.get(i);
                    fromServer.writeUTF(temp.getUsername());
                    fromServer.writeUTF(temp.getDisplayedName());
                    fromServer.writeUTF(String.valueOf(temp.getActiveStatus()));
                    fromServer.writeUTF(temp.getHost());
                    fromServer.writeUTF(String.valueOf(temp.getPort()));
                }

                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
           
    public String getUsernameOwningThread() {
        return this.usernameOwningThread;
    }
}
