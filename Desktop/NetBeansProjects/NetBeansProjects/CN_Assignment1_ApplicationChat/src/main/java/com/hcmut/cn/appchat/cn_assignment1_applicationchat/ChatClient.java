/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

import java.io.*;
import java.net.*;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ClientInfo;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ReadClientThread;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.WriteClientThread;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

/**
 *
 * @author nguye
 */
public class ChatClient {
    private ClientInfo serverInfo;
    private ClientInfo myInfo;
    private ServerSocket myServerSocket;
    private Socket mySocket;
    private String accountInfo;
    private DataInputStream toClient;
    private DataOutputStream fromClient;
    private List<ClientInfo> listClient = new ArrayList<ClientInfo>();
        
    public ChatClient(ClientInfo _serverInfo, ClientInfo _myInfo) {
        serverInfo = _serverInfo;
        myInfo = _myInfo;
    }
    
    public void setUpConnectionToServer() {
        try {
            Socket socket = new Socket(this.serverInfo.getHost(), this.serverInfo.getPort());
            System.out.println("Connected to the chat server");
            
            try {
                toClient = new DataInputStream(socket.getInputStream());
                fromClient = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                System.out.println("Error getting input stream: " + ex.getMessage());
                ex.printStackTrace();
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }
    
    public boolean verifyAccount(String username, char[] password) {
        String response = "";

        try {
            System.out.print(username + " " + new String(password));

            fromClient.writeUTF("Verify account");
            fromClient.writeUTF(username);
            fromClient.writeUTF(new String(password));
            fromClient.writeUTF(this.myInfo.getHost());
            fromClient.writeUTF(String.valueOf(this.myInfo.getPort()));
                       
            response = toClient.readUTF();
            System.out.print(response);            
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        if(response.equals("true")) return true;
        else return false;
    }
    
    public boolean createAccount(String username, char[] password, String displayedName) {
        String response = "";
        
        try {
            System.out.print(username + " " + new String(password) + " " + displayedName);

            fromClient.writeUTF("Create account");
            fromClient.writeUTF(username);
            fromClient.writeUTF(new String(password));
            fromClient.writeUTF(displayedName);
           
            response = toClient.readUTF();
            
            System.out.print(response);
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        if(response.equals("true")) return true;
        else return false;
    }
    
    public List<ClientInfo> getClientList() {
        try {         
            this.fromClient.writeUTF("Get list of users");

            int numOfClient = Integer.valueOf(this.toClient.readUTF());
            String clientUsername, clientDisplayedname, clientHost;
            boolean clientActiveStatus;
            int clientPort;

            for (int i = 0; i < numOfClient - 1; i++) {
                clientUsername = this.toClient.readUTF();
                clientDisplayedname = this.toClient.readUTF();
                clientActiveStatus = Boolean.valueOf(this.toClient.readUTF());
                clientHost = this.toClient.readUTF();
                clientPort = Integer.valueOf(this.toClient.readUTF());
                System.out.println(clientUsername + " " + clientDisplayedname);
                System.out.println(clientHost + " " + String.valueOf(clientPort));
                this.listClient.add(new ClientInfo(clientUsername, clientDisplayedname, clientActiveStatus, clientHost, clientPort));
            }

        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
            ex.printStackTrace();
        }
        return this.listClient;        
    }
            
    public void refreshClientList() {
        GetClientListThread getClientListThread = new GetClientListThread(this.fromClient, this.toClient);
            
        getClientListThread.start();
                
        synchronized(getClientListThread) {
            this.listClient = getClientListThread.getClientList();
            }
        
        synchronized(this) {
            notify();
        }
    }
    
    public Socket accept() {
        try {
            myServerSocket = new ServerSocket(myInfo.getPort());
            Socket tempSocket = myServerSocket.accept();
            
            myServerSocket.close();
            
            
            return tempSocket;
            
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            
            if (myServerSocket != null) {
                try {
                    myServerSocket.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex1);
                    ex.printStackTrace();
                }
            }
            
            return null;
        }

    }
    
    public Socket connectSocket(ClientInfo otherClient) {
        try {
            mySocket = new Socket(otherClient.getHost(), otherClient.getPort());
        } catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
        }
        return mySocket;
    }
    
    public static void main(String[] args) {}

    public String getMyHost() {
        return this.myInfo.getHost();
    }
    
    public int getMyPort() {
        return this.myInfo.getPort();
    }

    ClientInfo getClientInfo() {
        return myInfo;
    }
}
