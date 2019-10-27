/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vu Van Tien
 */
public class ClientInfo {
    
    private String myIP;
    private int myActivePORT;
    
    public ClientInfo(String IP, int PORT) {
            myIP = IP;
            myActivePORT = PORT;
    }

    public String getIP() {
        return myIP;
    }

    public int getPORT() {
        return myActivePORT;
    }
}
