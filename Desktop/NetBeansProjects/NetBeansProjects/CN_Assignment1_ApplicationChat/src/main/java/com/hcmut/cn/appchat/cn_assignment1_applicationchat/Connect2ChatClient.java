/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ChatClient;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ServerSocketThread;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ClientInfo;

/**
 *
 * @author Vu Van Tien
 */
public class Connect2ChatClient {
//    private ChatClient chatClient1;
//    private ChatClient chatClient2;
    
    private ClientInfo clientInfo1;
    private ClientInfo clientInfo2;
    
    private ServerSocket serverSocket1;
    private ServerSocket serverSocket2;
    
    private Socket socket12;
    private Socket socket21;
    
    private Socket socket21_accept;
    
//    public Connect2ChatClient(ChatClient chatClient1, ChatClient chatClient2) {
//        this.chatClient1 = chatClient1;
//        this.chatClient2 = chatClient2;
//        
//        serverSocket1 = this.createServerSocket(chatClient1.getMyPort());
//        serverSocket2 = this.createServerSocket(chatClient2.getMyPort());
//        
//        this.createConnection();
//    }
    
    public Connect2ChatClient(ClientInfo clientInfo1, ClientInfo clientInfo2) {
        this.clientInfo1 = clientInfo1;
        this.clientInfo2 = clientInfo2;
    }
    
    private ServerSocket createServerSocket(int port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            return serverSocket;
        } catch (IOException ex) {
            Logger.getLogger(Connect2ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private void createConnection() {
        ServerSocketThread serverSocketThread1 = new ServerSocketThread(serverSocket1);
        ServerSocketThread serverSocketThread2 = new ServerSocketThread(serverSocket2);
        
        serverSocketThread1.start();
        serverSocketThread2.start();
        
        try {
            socket12 = new Socket(clientInfo2.getIP(), clientInfo2.getPort());
        } catch (IOException ex) {
            Logger.getLogger(Connect2ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            socket21 = new Socket(clientInfo1.getIP(), clientInfo1.getPort());
        } catch (IOException ex) {
            Logger.getLogger(Connect2ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        socket21_accept = serverSocketThread2.getSocket();
                
    }
    
    public Socket getSocket12() {
        return socket12;
    }
    public Socket getSocket21() {
        return socket21;
    }
    public ServerSocket getServerSocket1() {
        return serverSocket1;
    }
    public ServerSocket getServerSocket2() {
        return serverSocket2;
    }
    public Socket getSocket21Accept() {
        return socket21_accept;
    }
}
