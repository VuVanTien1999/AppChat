/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

/**
 *
 * @author nguye
 */
public class AccountInfo {
    private String username, displayedName;
    
    private boolean activeStatus;
    
    public AccountInfo(String username, String password, String displayedName) {
        this.displayedName = displayedName;
    }

    public void setActiveStatus(boolean status) {
        this.activeStatus = status;
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
}
