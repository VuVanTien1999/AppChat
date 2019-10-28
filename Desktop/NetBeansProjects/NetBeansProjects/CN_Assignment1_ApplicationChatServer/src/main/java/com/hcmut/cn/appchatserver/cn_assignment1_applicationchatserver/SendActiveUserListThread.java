/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchatserver.cn_assignment1_applicationchatserver;

import java.io.*;
import java.util.*;

/**
 *
 * @author nguye
 */

public class SendActiveUserListThread extends Thread { 
    private ChatServer server;
    private DataOutputStream fromServer;
    
    public SendActiveUserListThread(ChatServer server, DataOutputStream fromServer) {
        this.server = server;
        this.fromServer = fromServer;
    }
    
    public void run() {
        try {
            while(true) {
                List<AccountProfile> listActiveUser = this.server.getUserProfile();
                fromServer.writeUTF(String.valueOf(listActiveUser.size()));    

                for (int i = 0 ; i < listActiveUser.size(); i++) {
                    AccountProfile temp = listActiveUser.get(i);
                    fromServer.writeUTF(temp.getDisplayedName());
                    fromServer.writeUTF(String.valueOf(temp.equals(i)));
                }

                try {
                    Thread.sleep(20 * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }    
}
