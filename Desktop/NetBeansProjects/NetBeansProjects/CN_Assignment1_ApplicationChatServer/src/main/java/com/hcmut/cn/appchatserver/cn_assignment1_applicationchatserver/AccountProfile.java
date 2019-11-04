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
    private int numOfFriend = 0, numOfFriendRequest = 0;
    private boolean activeStatus = false;
    private List<String> friendList = new ArrayList<>();
    private List<String> friendRequestList = new ArrayList<>();
    private String host = "None";
    private int port = 0;
        
    public AccountProfile(String username, String password, String displayedName) {
        this.username = username;
        this.password = password;
        this.displayedName = displayedName;
    }
    
    public AccountProfile(String username, String password, String displayedName, int numOfFriend, String[] friends) {
        this(username, password, displayedName);
        this.numOfFriend = numOfFriend;
        
        for (int i = 0; i < numOfFriend; i++) {
            this.friendList.add(friends[i]);
        }
    }

    public boolean isAccountExisted(String username) {
        return this.username.equals(username);
    }
    
    public boolean isAccountValid(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
    public boolean isFriend(String username) {
        return this.friendList.contains(username);
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
    
    public int getNumOfFriendRequest() {
        return this.numOfFriendRequest;
    }
    
    public String[] getFriendUsername() {
        String[] result = new String[this.numOfFriend];
        
        for(int i = 0; i < this.numOfFriend; i++) {
            result[i] = this.friendList.get(i);
        }
        
        return result;
    }
    
    public String[] getFriendReuqestUsername() {
        String[] result = new String[this.numOfFriendRequest];
        
        for(int i = 0; i < this.numOfFriendRequest; i++) {
            result[i] = this.friendRequestList.get(i);
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
        
    public void acceptFriend(String newFriendUsername) {
        this.numOfFriendRequest--;
        this.friendRequestList.remove(newFriendUsername);
    }
    
    public void addFriend(String newFriendUsername) {
        this.numOfFriend++;
        this.friendList.add(newFriendUsername);
    }
    
    public void addNewFriendRequest(String newFriendUsername) {
        this.numOfFriendRequest++;
        this.friendRequestList.add(newFriendUsername);
    }
    
    public void declineFriendRequest(String friendUsername){
        this.numOfFriendRequest--;
        this.friendRequestList.remove(friendUsername);
    }
    
    public boolean isRequestExited(String requestUsername) {
        return this.friendRequestList.contains(requestUsername);
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public void setPort(int port) {
        this.port = port;
    } 
}
