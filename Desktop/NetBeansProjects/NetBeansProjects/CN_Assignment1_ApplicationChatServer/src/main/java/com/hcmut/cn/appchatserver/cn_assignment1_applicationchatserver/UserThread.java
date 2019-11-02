/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hcmut.cn.appchatserver.cn_assignment1_applicationchatserver;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Viet Hoang Nguyen
 * 
 */

public class UserThread extends Thread {
    private Socket serverSocket;
    private ChatServer server;

    private String usernameThread;
    
    private DataInputStream toServer;
    private DataOutputStream fromServer;
        
    public UserThread(ChatServer server, Socket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
    }
    
    public void run() {
        try {
            toServer = new DataInputStream(serverSocket.getInputStream());
            fromServer = new DataOutputStream(serverSocket.getOutputStream());
            
            do {
                String temp = toServer.readUTF(); 
                
                if (temp.equals("Create account")) {
                    String username, password, displayedName;
                
                    username = toServer.readUTF();
                    password = toServer.readUTF();
                    displayedName = toServer.readUTF();

                    System.out.print(System.getProperty("line.separator") + "Create: " + username + " " + password + " " + displayedName);

                    //Write acocunt info to file
                    if (server.isAccountExisted(username, password, displayedName)) {
                        fromServer.writeUTF("false");
                    }
                    else {
                        fromServer.writeUTF("true");
                    }
                }
                else if (temp.equals("Verify account")) {
                    String username, password, host;
                    int port;
                
                    username = toServer.readUTF();
                    password = toServer.readUTF();
                    host = toServer.readUTF();
                    port = Integer.valueOf(toServer.readUTF());

                    System.out.print(System.getProperty("line.separator") + "Verify: " + username + " " + password);

                    //Read file and find acocunt info 
                    if (server.isAccountValid(username, password, host, port)) {
                        this.usernameThread = username;
                        fromServer.writeUTF("true");
                        break;
                    }
                    else fromServer.writeUTF("false");
                }
            } while(true);
            
            List<AccountProfile> tempList = this.server.getUserList();
            for (int i = 0; i < tempList.size(); i++) {
                System.out.println(tempList.get(i).getUsername());
                System.out.println(String.valueOf(tempList.get(i).getActiveStatus()));
            }
            
            List<AccountProfile> userList;
            while(true) {
                userList = this.server.getUserList();
                
                AccountProfile myProfile = userList.stream()
                            .filter(user -> user.getUsername().equals(this.usernameThread))
                            .findAny()
                            .orElse(null);
                
                if (toServer.readUTF().equals("Get list of users")) {
                    //fromServer.writeUTF("Return list of users");
                    
                    // Return number of friend
                    fromServer.writeUTF(String.valueOf(myProfile.getNumOfFriend()));    

                    String[] listFriendsUsername = myProfile.getFriendUsername();    
                    for (int i = 0; i < myProfile.getNumOfFriend(); i++) {
                        String friendUsername = listFriendsUsername[i];

                        AccountProfile temp = userList.stream()
                                .filter(user -> user.getUsername().equals(friendUsername))
                                .findAny()
                                .orElse(null);
                        // Return friend's info (Username, Displayed name, Active status, Host, Port)
                        fromServer.writeUTF(temp.getUsername());
                        fromServer.writeUTF(temp.getDisplayedName());
                        fromServer.writeUTF(String.valueOf(temp.getActiveStatus()));
                        fromServer.writeUTF(temp.getHost());
                        fromServer.writeUTF(String.valueOf(temp.getPort()));
                    } 
                }
                else if (toServer.readUTF().equals("Get list of friend requests")) {
                    fromServer.writeUTF("Return list of friend requests");
                    
                    // Return number of friend request
                    fromServer.writeUTF(String.valueOf(myProfile.getNumOfFriendRequest()));    

                    String[] listFriendRequestsUsername = myProfile.getFriendUsername();    
                    for (int i = 0; i < myProfile.getNumOfFriendRequest(); i++) {
                        // Return friend request's info (Username)
                        fromServer.writeUTF(listFriendRequestsUsername[i]);
                    }
                }
                else if (toServer.readUTF().equals("Send friend request")) {
                    String friendRequestUsername = toServer.readUTF();
                    
                    if (this.server.isAccountExisted(friendRequestUsername)) {
                        AccountProfile friendProfile = userList.stream()
                           .filter(user -> user.getUsername().equals(friendRequestUsername))
                           .findAny()
                           .orElse(null);
                        
                        friendProfile.addNewFriendRequest(this.usernameThread);
                    }
                    else {
                        fromServer.writeUTF("Username isn't existed");
                    }
                }
                else if (toServer.readUTF().equals("Accept friend request")) {
                    String friendRequestUsername = toServer.readUTF();
                    
                    myProfile.deleteFriendRequest(friendRequestUsername);
                    myProfile.addNewFriend(friendRequestUsername);
                    
                    AccountProfile friendProfile = userList.stream()
                           .filter(user -> user.getUsername().equals(friendRequestUsername))
                           .findAny()
                           .orElse(null);
                    friendProfile.addNewFriend(this.usernameThread);
                    
                    this.server.writeToAccountList(this.usernameThread, friendRequestUsername);
                    this.server.writeToAccountList(friendRequestUsername, this.usernameThread);
                }
                else if (toServer.readUTF().equals("Decline friend request")) {
                    String friendSendingRequestUsername = toServer.readUTF();
                    
                    myProfile.deleteFriendRequest(friendSendingRequestUsername);
                }
            }
        } catch (IOException ex) {
            AccountProfile offlineUser = this.server.getUserList().stream()
                    .filter(user -> user.getUsername().equals(this.usernameThread))
                    .findAny()
                    .orElse(null);
            offlineUser.setActiveStatus(false);
            offlineUser.setHost("None");
            offlineUser.setPort(0);
            
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }  
}
