/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hcmut.cn.appchatserver.cn_assignment1_applicationchatserver;

import java.io.*;
import java.net.*;
import java.util.*;

import javafx.util.Pair;

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

                    //Write acocunt info to file
                    if (server.isAccountExisted(username)) {
                        fromServer.writeUTF("false");
                    }
                    else {
                        server.createAccount(username, password, displayedName);
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

                    //Read file and find acocunt info 
                    if (server.isAccountValid(username, password)) {
                        Pair<String, String> result = server.isAccountOnline(username);
                        if (result.getKey().equals("true")) {
                            fromServer.writeUTF("Login somewhere else");
                        }
                        else {
                            this.usernameThread = username;
                            this.server.setInfoForOnlineUser(username, host, port);
                            fromServer.writeUTF("true");
                            break;
                        }
                    }
                    else fromServer.writeUTF("false");
                }
            } while(true);
            
            List<AccountProfile> userList;
            while(true) {
                userList = this.server.getUserList();
                
                AccountProfile myProfile = userList.stream()
                            .filter(user -> user.getUsername().equals(this.usernameThread))
                            .findAny()
                            .orElse(null);
                
                String incomingMessage = toServer.readUTF();
                
                if (incomingMessage.equals("Get list of users")) {
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
                else if (incomingMessage.equals("Get list of friend requests")) {
                    //fromServer.writeUTF("Return list of friend requests");
                    
                    // Return number of friend request
                    //System.out.println("Req: " + String.valueOf(myProfile.getNumOfFriendRequest()));
                    fromServer.writeUTF(String.valueOf(myProfile.getNumOfFriendRequest()));    
                    //System.out.println("Req: " + String.valueOf(myProfile.getNumOfFriendRequest()));
                    String[] listFriendRequestsUsername = myProfile.getFriendReuqestUsername();    
                    for (int i = 0; i < myProfile.getNumOfFriendRequest(); i++) {
                        // Return friend request's info (Username)
                        fromServer.writeUTF(listFriendRequestsUsername[i]);
                    }
                }
                else if (incomingMessage.equals("Send friend request")) {
                    String friendRequestUsername = toServer.readUTF();
                    
                    Pair<String, String> result = this.server.addFriendRequest(usernameThread, friendRequestUsername);
                    
                    if (result.getKey().equals("false")) {
                        if (result.getValue().equals("Friend account isn't existed")) {
                            fromServer.writeUTF("Username isn't existed");
                        }
                        if (result.getValue().equals("You're friend already")) {
                            fromServer.writeUTF("You guys have been friend already");
                        }
                        else if (result.getValue().equals("Request has been sent before")) {
                            fromServer.writeUTF("Request has been sent before");
                        }
                    }
                    else {
                        fromServer.writeUTF("Request has been sent successfully");
                    }
                }
                else if (incomingMessage.equals("Accept friend request")) {
                    String friendRequestUsername = toServer.readUTF();
                    System.out.println(friendRequestUsername);
                    this.server.acceptFriend(usernameThread, friendRequestUsername);
                    
                    fromServer.writeUTF(friendRequestUsername + " and " + this.usernameThread + " has become friends");
                }
                else if (incomingMessage.equals("Decline friend request")) {
                    String friendSendingRequestUsername = toServer.readUTF();
                    
                    this.server.declineFriendRequest(usernameThread, friendSendingRequestUsername);
                    
                    fromServer.writeUTF("You have declined friend request of " + friendSendingRequestUsername);
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
