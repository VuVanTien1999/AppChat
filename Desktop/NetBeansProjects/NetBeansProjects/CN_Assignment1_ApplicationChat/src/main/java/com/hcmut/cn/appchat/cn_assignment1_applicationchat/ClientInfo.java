/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

/**
 *
 * @author Vu Van Tien
 * 
 */
public class ClientInfo {
    private String username, displayedName, host = "";
    private boolean activeStatus = false;
    private int port = 0;
    
    public ClientInfo(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public ClientInfo(String username, String displayedName, boolean activeStatus, String host, int port) {
        this.username = username;
        this.displayedName = displayedName;
        this.activeStatus = activeStatus;
        this.host = host;
        this.port = port;
    }

    public String getUsername() {
        return this.username;
    }
    
    public String getDisplayedName() {
        return this.displayedName;
    } 
    
    public boolean getActiveStatus() {
        return this.activeStatus;
    }
    
    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }
    
    public void setActiveStatus(boolean status) {
        this.activeStatus = status;
    }
    
    public void setHostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }    
}
