/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ChatClient;
import java.net.*;
import java.io.*;
        
/**
 *
 * @author Vu Van Tien
 */
public class Client1 {
    public static void main(String[] args) {
        ClientInfo client1Info = new ClientInfo("localhost", 5001);
        ClientInfo client2Info = new ClientInfo("localhost", 5002);
        ClientInfo serverInfo = new ClientInfo("localhost", 9999);
        
        ChatClient client1 = new ChatClient(serverInfo, client1Info);
        
        Socket connect12 = client1.connectSocket(client2Info);
        
        
        // SendFileThread(connect12)
        
        System.out.println("Client1");
        
        ChatWindow client1WD = new ChatWindow(connect12, "Client1");
        client1WD.setVisible(true);
        
        
    }
}
