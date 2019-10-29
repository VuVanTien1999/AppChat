/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ChatWindow;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author talaq
 */
public class ChatWindowThread extends Thread {
    private ServerSocket serverSocket;
    private Socket socketMessage;
    private Socket socketSendFile;
    

    ChatWindowThread(ServerSocket serverSocket, Socket socketMessage, Socket socketSendFile) {
        this.serverSocket = serverSocket;
        this.socketMessage = socketMessage;
        this.socketSendFile = socketSendFile;
    }
    
    public void run() {
        ChatWindow chatWindow = new ChatWindow(serverSocket, socketMessage, socketSendFile);
    }
}
