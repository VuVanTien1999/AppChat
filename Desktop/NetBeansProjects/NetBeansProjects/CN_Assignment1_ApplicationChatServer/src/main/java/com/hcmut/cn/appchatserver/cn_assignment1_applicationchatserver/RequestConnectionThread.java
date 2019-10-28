/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchatserver.cn_assignment1_applicationchatserver;

import java.io.*;
import java.util.*;
import javafx.util.Pair;

/**
 *
 * @author nguye
 */
public class RequestConnectionThread extends Thread {
    private ChatServer server;
    private DataInputStream toServer;
    private DataOutputStream fromServer;
    private String requiringUser, requiredUser;
    
    public RequestConnectionThread(ChatServer server, DataInputStream toServer, DataOutputStream fromServer, String requiringUser, String requiredUser) {
        this.server = server;
        this.toServer = toServer;
        this.fromServer = fromServer;
        this.requiringUser = requiringUser;
        this.requiredUser = requiredUser;
    }
        
    public void run() {
        Pair<String, Integer> requiredAccount = this.server.requestToClient(this.requiringUser, this.requiredUser);
                    
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

        String IP = requiredAccount.getKey();
        int port = requiredAccount.getValue();

        try {
            if (!IP.equals("") && (port != 0)) fromServer.writeUTF("Requirement denied");
            else {
                fromServer.writeUTF("Requirement accepted");
                fromServer.writeUTF(IP);
                fromServer.writeUTF(String.valueOf(port));
            }
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
