/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author nguye
 */
public class ReadClientThread extends Thread {
    private Socket clientSocket;
    private DataInputStream clientIn;
    
    private JTextArea text_msg_disp = null;
    
    public ReadClientThread(Socket clSocket) {
        // clSocket: socket toi ChatClient can Chat
        // cl: ChatClient dang o, socket ket noi toi ChatClient muon chat
        
        this.clientSocket = clSocket;
        //this.client = cl;
 
        try {
            clientIn = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void run() {
        while (true) {
            try {
                if (clientIn.available() > 0) {
                    try {
                        String response = clientIn.readUTF();
                        System.out.println("\n" + response);
                        
                        if (text_msg_disp!=null) {
                            text_msg_disp.append(response + "\n");
                        }
                        
                    } catch (IOException ex) {
                        System.out.println("Error reading from server: " + ex.getMessage());
                        ex.printStackTrace();
                        break;
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                        ex.printStackTrace();
                        break;
            }
            
        }
    }

    void setTextArea(JTextArea area_msg_disp) {
        text_msg_disp = area_msg_disp;
    }
}
