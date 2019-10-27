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

/**
 *
 * @author nguye
 */
public class ChatClient {
    private ClientInfo serverInfo;
    private ClientInfo myInfo;
    private ServerSocket myServerSocket;
    private Socket mySocket;
        
    public ChatClient(ClientInfo _serverInfo, ClientInfo _myInfo) {
        serverInfo = _serverInfo;
        myInfo = _myInfo;
        
    }
    
    private String accountInfo;
    private DataInputStream clientIn;
    private DataOutputStream clientOut;
    
    public void setUpConnection() {
        try {
            Socket socket = new Socket(serverInfo.getIP(), serverInfo.getPORT());
            System.out.println("Connected to the chat server");
            
            try {
                clientIn = new DataInputStream(socket.getInputStream());
                clientOut = new DataOutputStream(socket.getOutputStream());
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
    
    public void verifyAccount(String inputUNPW) {
        this.accountInfo = inputUNPW;

        System.out.print(this.accountInfo);

        try {
            clientOut.writeUTF(this.accountInfo);
        } catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
        }
             
        // new ReadClientThread(socket, this).start();
        // new WriteClientThread(socket, this).start();
    }
    
    public Socket accept() {

        //        try {
//            myServerSocket = new ServerSocket(myInfo.getPORT());
//            
//        } catch (IOException ex) {
//            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("ChatClient Constructor error");
//            ex.printStackTrace();
//            
//            return null;
//        }
//        
//        try {
//            // wait for upcoming socket from other Client
//            Socket tempSocket = myServerSocket.accept(); // return socket?
//            
//            myServerSocket.close();
//            
//            return tempSocket;
//            
//        } catch (IOException ex) {
//            System.out.println("Error reading from server: " + ex.getMessage());
//            ex.printStackTrace();
//
//            
//        }
//        finally {
//            try {
//                myServerSocket.close();
//            } catch (IOException ex) {
//                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
//        }
//        
//        return null;
        try {
            myServerSocket = new ServerSocket(myInfo.getPORT());
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
            mySocket = new Socket(otherClient.getIP(), otherClient.getPORT());
        } catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
        }
        return mySocket;
    }
    
    public static void main(String[] args) {
        ClientInfo client1Info = new ClientInfo("localhost", 5001);
        ClientInfo client2Info = new ClientInfo("localhost", 5002);
        ClientInfo serverInfo = new ClientInfo("localhost", 9999);
        
        ChatClient client1 = new ChatClient(serverInfo, client1Info);
        ChatClient client2 = new ChatClient(serverInfo, client2Info);
        
        // client1 gui yeu cau connect toi client2 (thong qua server)
        // client2 chap nhan ket noi
//        System.out.println("Hello");
//        
//        Socket connect21 = client2.accept();
//        Socket connect12 = client1.connectSocket(client2Info);
//        
//        ReadClientThread client1_read = new ReadClientThread(connect12);
//        WriteClientThread client1_write = new WriteClientThread(connect12);
//        
//        ReadClientThread client2_read = new ReadClientThread(connect21);
//        WriteClientThread client2_write = new WriteClientThread(connect21);
        
        
        
        // check set up? when open socket? when close socket?
        // ...
        
        // when create ReadClientThread?
        // when create WriteClientThread?
        
        // read and write thread for client 1
        
        
        
        
    }

    int getMyPort() {
        return myInfo.getPORT();
    }

    public boolean acceptConnect() {
        return true;
    }

    String getMyIP() {
        return myInfo.getIP();
    }
}
