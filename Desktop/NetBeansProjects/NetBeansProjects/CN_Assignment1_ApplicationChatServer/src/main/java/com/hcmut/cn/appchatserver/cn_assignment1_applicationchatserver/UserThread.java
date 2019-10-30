/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hcmut.cn.appchatserver.cn_assignment1_applicationchatserver;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Viet Hoang Nguyen
 * 
 */

public class UserThread extends Thread {
    private Socket serverSocket;
    private ChatServer server;

    private String usernameThread;
    
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

                    //Read file and find acocunt info 
                    if (server.isAccountValid(username, password, host, port)) {
                        this.usernameThread = username;
                        fromServer.writeUTF("true");
                        break;
                    }
                    else fromServer.writeUTF("false");
                }
            } while(true);
            
            List<AccountProfile> userList;
            while(true) {
                userList = this.server.getUserList();
                fromServer.writeUTF(String.valueOf(userList.size()));    

                for (int i = 0 ; i < userList.size(); i++) {
                    AccountProfile temp = userList.get(i);

                    if (temp.getUsername().equals(this.usernameThread)) continue;
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
}
