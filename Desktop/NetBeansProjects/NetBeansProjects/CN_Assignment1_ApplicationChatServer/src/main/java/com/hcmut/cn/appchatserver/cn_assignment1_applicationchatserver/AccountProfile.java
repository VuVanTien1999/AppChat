/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hcmut.cn.appchatserver.cn_assignment1_applicationchatserver;

/**
 *
 * @author nguye
 */

public class AccountProfile {
    private String username, password, displayedName;
    
    private boolean activeStatus;
    
    private String IP;
    private int port;
    
    public AccountProfile(String username, String password, String displayedName) {
        this.username = username;
        this.password = password;
        this.displayedName = displayedName;
        this.activeStatus = false;
    }

    public boolean isAccountExisted(String username) {
        return this.username.equals(username);
    }
    
    public boolean isAccountValid(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
    public void setActiveStatus(boolean status) {
        this.activeStatus = status;
    }
    
    public void setIP(String IP) {
        this.IP = IP;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public boolean getActiveStatus() {
        return this.activeStatus;
    }
    
    public String getIP() {
        return this.IP;
    }    
    
    public int getPort() {
        return this.port;
    }
    
    public String getDisplayedName() {
        return this.displayedName;
    }       
}
