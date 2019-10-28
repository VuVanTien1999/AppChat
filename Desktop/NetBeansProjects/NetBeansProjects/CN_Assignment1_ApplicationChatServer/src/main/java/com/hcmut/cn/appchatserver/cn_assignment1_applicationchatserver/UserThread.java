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
 * @author nguye
 */
public class UserThread extends Thread {
    private Socket serverSocket;
    private ChatServer server;

    private DataInputStream serverIn;
    private DataOutputStream serverOut;
    
    public UserThread(Socket socket, ChatServer server) {
        this.serverSocket = socket;
        this.server = server;
    }
 
    public void run() {
        try {
            serverIn = new DataInputStream(serverSocket.getInputStream());
            serverOut = new DataOutputStream(serverSocket.getOutputStream());

            String uspa = serverIn.readUTF(); 
            
            System.out.print(uspa);
            
            String clientMessage;
 
            do {
                clientMessage = serverIn.readUTF();
            } while (!clientMessage.equals("bye"));
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
