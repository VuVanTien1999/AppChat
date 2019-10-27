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
public class Client2 {
    public static void main(String[] args) {
        ClientInfo client1Info = new ClientInfo("localhost", 5001);
        ClientInfo client2Info = new ClientInfo("localhost", 5002);
        ClientInfo serverInfo = new ClientInfo("localhost", 9999);
        
        
        ChatClient client2 = new ChatClient(serverInfo, client2Info);
        
        Socket connect21 = client2.accept();
        
//        ReadClientThread client2_read = new ReadClientThread(connect21);
//        WriteClientThread client2_write = new WriteClientThread(connect21);
        
        System.out.println("Client2");
        
        ChatWindow client2WD = new ChatWindow(connect21, "Client2");
        client2WD.setVisible(true);
    }
}
