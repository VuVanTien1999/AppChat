/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ObjectOutputStream;

/**
 *
 * @author Vu Van Tien
 */
class ParallelSendThread extends Thread {
    private File file;
    private int port;
    private String hostname;
    private ClientInfo myInfo;

    public ParallelSendThread(File file) {
        this.file = file;
    }

    ParallelSendThread(File file, ClientInfo otherInfo, ClientInfo myInfo) {
        this(file);
        port = otherInfo.getPort();
        hostname = otherInfo.getHost();
        this.myInfo = myInfo;
    }
    
    public void run() {
        try {
            Socket newSocket = new Socket(hostname, port);
            
            // Send username for otherClient to get responding ChatWindow
            DataOutputStream clientOut = new DataOutputStream(newSocket.getOutputStream());
            clientOut.writeUTF(this.myInfo.getUsername());
            //clientOut.close();
            
            ObjectOutputStream objOut = new ObjectOutputStream(newSocket.getOutputStream());
            objOut.writeObject(file);
            
        } catch (IOException ex) {
            Logger.getLogger(ParallelSendThread.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}
