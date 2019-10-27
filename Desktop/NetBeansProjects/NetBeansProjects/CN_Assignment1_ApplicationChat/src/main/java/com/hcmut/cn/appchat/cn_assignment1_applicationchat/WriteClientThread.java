/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

import java.io.*;
import java.net.*;

/**
 *
 * @author nguye
 */
public class WriteClientThread extends Thread {
    private Socket clientSocket;
    private DataOutputStream clientOut;
    
    public WriteClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
 
        try {
            clientOut = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    // Hoang?
    
    public void run() {
        while (true) {
            try {
                String str = "Hello world";
                clientOut.writeUTF(str);
                System.out.println("\n" + str);
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }

    void write(String str) {
        try {
                clientOut.writeUTF(str);
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
            }
    }
}
