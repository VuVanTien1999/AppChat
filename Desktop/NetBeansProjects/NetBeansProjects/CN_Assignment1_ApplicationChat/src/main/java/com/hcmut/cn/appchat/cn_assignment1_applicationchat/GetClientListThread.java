/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

import java.io.*;
import java.util.List;

/**
 *
 * @author talaq
 */
public class GetClientListThread extends Thread {
    private DataOutputStream fromClient;
    private DataInputStream toClient;
    private List<ClientInfo> listClient;
    
    public GetClientListThread(DataOutputStream fromClient, DataInputStream toClient) {
        this.fromClient = fromClient;
        this.toClient = toClient;
    }
    
    public void run() {
        while(true) {
            try {         
                this.fromClient.writeUTF("Get list of users");

                int numOfClient = Integer.valueOf(this.toClient.readUTF());
                String clientUsername, clientDisplayedname, clientHost;
                boolean clientActiveStatus;
                int clientPort;

                for (int i = 0; i < numOfClient; i++) {
                    clientUsername = this.toClient.readUTF();
                    clientDisplayedname = this.toClient.readUTF();
                    clientActiveStatus = Boolean.valueOf(this.toClient.readUTF());
                    clientHost = this.toClient.readUTF();
                    clientPort = Integer.valueOf(this.toClient.readUTF());
                    System.out.print(clientUsername + clientDisplayedname);
                    this.listClient.add(new ClientInfo(clientUsername, clientDisplayedname, clientActiveStatus, clientHost, clientPort));
                }

            } catch (IOException ex) {
                System.out.println("Error writing to server: " + ex.getMessage());
                ex.printStackTrace();
            }
            synchronized(this){    
                notify();
            }
            try {
                Thread.sleep(20 * 1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public List<ClientInfo> getClientList() {
        return this.listClient;
    }
}
