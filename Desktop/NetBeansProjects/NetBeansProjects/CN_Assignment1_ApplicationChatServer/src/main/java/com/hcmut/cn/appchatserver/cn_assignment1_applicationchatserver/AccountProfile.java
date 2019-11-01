/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hcmut.cn.appchatserver.cn_assignment1_applicationchatserver;

import java.util.*;

/**
 *
 * @author Viet Hoang Nguyen
 * 
 */

public class AccountProfile {
    private String username, password, displayedName; 
    private int numOfFriend = 0;
    private boolean activeStatus = false;
    private List<String> friends = new ArrayList<>();
    private String host = "None";
    private int port = 0;
        
    public AccountProfile(String username, String password, String displayedName, int numOfFriend, String[] friends) {
        this.username = username;
        this.password = password;
        this.displayedName = displayedName;
        this.numOfFriend = numOfFriend;
        
        for (int i = 0; i < numOfFriend; i++) {
            this.friends.add(friends[i]);
        }
    }

    public boolean isAccountExisted(String username) {
        return this.username.equals(username);
    }
    
    public boolean isAccountValid(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
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
    
    public int getNumOfFriend() {
        return this.numOfFriend;
    }
    
    public String[] getFriendUsername() {
        String[] result = new String[this.numOfFriend];
        
        for(int i = 0; i < this.numOfFriend; i++) {
            result[i] = this.friends.get(i);
        }
        
        return result;
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
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public void setPort(int port) {
        this.port = port;
    } 
}
