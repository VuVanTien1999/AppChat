/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchatserver.cn_assignment1_applicationchatserver;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import javafx.util.*;

/**
 *
 * @author Viet Hoang Nguyen
 * 
 */

public class ChatServer {
    public final static int PORT = 9000;
    
    private List<AccountProfile> userList = new ArrayList<AccountProfile>();
    private List<UserThread> userThreads = new ArrayList<UserThread>();
 
    public ChatServer() {}
    
    public void execute() {
        this.userList = this.readFromAccountList();
        
        try (ServerSocket serverSocket = new ServerSocket(this.PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                
                UserThread newUser = new UserThread(this, socket);
                userThreads.add(newUser);
                
                newUser.start();
            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    // File contains info of all accounts which are signed up through server
    // Each account info is stored in file by two lines
    // First line is all about account info: "username" "," "password" "," displayed name" "," "number of friend"
    // Second line is the list of friends: "username of friend 1" "," "username of friend 2" "," ...
    private List<AccountProfile> readFromAccountList() {
        String fileName = "AccountList.txt";
                
        List<AccountProfile> userList = new ArrayList<AccountProfile>();
        
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String accountLine, friendLine;
            
            // First two lines of file is the header
            accountLine = bufferedReader.readLine();
            friendLine = bufferedReader.readLine();
            
            while((accountLine = bufferedReader.readLine()) != null) {             
                String[] parts = accountLine.split(",", 4);
                String username = parts[0];
                String password = parts[1];
                String displayedname = parts[2];
                int numOfFriend = Integer.valueOf(parts[3]);
                
                friendLine = bufferedReader.readLine();   
                String[] friends = friendLine.split(",", numOfFriend);
                
                userList.add(new AccountProfile(username, password, displayedname, numOfFriend, friends));
            }   

            bufferedReader.close();  
            fileReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        return userList;
    }
    
    private void writeToAccountLine(String username, String password, String displayedName) {
        String fileName = "AccountList.txt";

        try {
            FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                                    
            bufferedWriter.write(username + "," + password + "," + displayedName + "," + "0");
            bufferedWriter.newLine();
            bufferedWriter.write("");
            bufferedWriter.newLine();

            bufferedWriter.close();
            fileWriter.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    private void writeToFriendLine(String username, String newFriendUsername) {
        File fileName = new File("Accountlist.txt");
        
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(fileName.toPath(), StandardCharsets.UTF_8));
        
            for (int i = 2; i < fileContent.size(); i += 2) {
                String parts[] = fileContent.get(i).split(",", 4);                            
                int numOfFriend = Integer.valueOf(parts[3]);
                
                if (parts[0].equals(username)) {
                    String accountProfileUpdate = "";
                    String friendUpdate = "";
                    
                    numOfFriend++;
                    accountProfileUpdate = parts[0] + "," + parts[1] + "," + parts[2] + "," + String.valueOf(numOfFriend);
                    
                    if (numOfFriend == 1) {
                        friendUpdate = newFriendUsername;
                    }
                    else {                        
                        friendUpdate = fileContent.get(i + 1) + "," + newFriendUsername;
                    }
                    
                    fileContent.set(i, accountProfileUpdate);
                    fileContent.set(i + 1, friendUpdate);               
                }
            }
            
            Files.write(fileName.toPath(), fileContent, StandardCharsets.UTF_8);
        } catch(IOException ex) {
            ex.printStackTrace();
        }    
    }
    
    public boolean isAccountExisted(String username) {
        AccountProfile existedAccount = this.userList.stream()
                .filter(user -> user.isAccountExisted(username))
                .findAny()
                .orElse(null);
        
        if (existedAccount == null) return false;
        return true;
    }
            
    public boolean isAccountValid(String username, String password) {
        AccountProfile validAccount = this.userList.stream()
                    .filter(user -> user.isAccountValid(username, password))
                    .findAny()
                    .orElse(null);
                
        if (validAccount == null) return false;
        return true;
    }
    
    public Pair<String, String> isAccountOnline(String username) {
        String bool = "", notice = "";
        
        AccountProfile onlineAccount = this.userList.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findAny()
                    .orElse(null);
        
        if (onlineAccount == null) {
            bool = "false";
            notice = "User account isn't existed";
        }
        else {
            if (onlineAccount.getActiveStatus()) {
                bool = "true";
            }
            else {
                bool = "false";
                notice = "User account isn't online";
            }
        }
        
        return new Pair<String, String>(bool, notice);
    }
    
    public boolean setInfoForOnlineUser(String username, String host, int port) {
        boolean result = true;
        
        AccountProfile validAccount = this.userList.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findAny()
                    .orElse(null);
        
        if (validAccount != null) {
            validAccount.setActiveStatus(true);
            validAccount.setHost(host);
            validAccount.setPort(port);
        }        
        else result = false;
        
        return result;
    }
    
    public boolean createAccount(String username, String password, String displayedName) {       
        if (!this.isAccountExisted(username)) {
            writeToAccountLine(username, password, displayedName);
            this.userList.add(new AccountProfile(username, password, displayedName));
            return true;
        }
        return false;
    }
    
    public void acceptFriend(String username, String newFriendUsername) { 
        AccountProfile myAccount = this.userList.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findAny()
                    .orElse(null);
        
        myAccount.acceptFriend(newFriendUsername);
        myAccount.addFriend(newFriendUsername);
        
        AccountProfile friendAccount = this.userList.stream()
                    .filter(user -> user.getUsername().equals(newFriendUsername))
                    .findAny()
                    .orElse(null);
        
        friendAccount.addFriend(username);
        
        this.writeToFriendLine(username, newFriendUsername);
        this.writeToFriendLine(newFriendUsername, username);
    }
        
    public Pair<String, String> addFriendRequest(String username, String newFriendUsername) {
        String bool = "";
        String notice = "";
        
        if (this.isAccountExisted(newFriendUsername)) {
            AccountProfile friendAccount = this.userList.stream()
                    .filter(user -> user.getUsername().equals(newFriendUsername))
                    .findAny()
                    .orElse(null);
            
            if (friendAccount.isFriend(username)) {
                bool = "false";
                notice = "You're friend already";
            }
            else if (friendAccount.isRequestExited(username)) {
                bool = "false";
                notice = "Request has been sent before";
            }
            else {
                friendAccount.addNewFriendRequest(username);
                
                bool = "true";
                notice = "Request has been sent now";
            }
        }
        else {
            bool = "false";
            notice = "Friend account isn't existed";
        }
        
        return new Pair<String, String>(bool, notice);
    }
    
    public void declineFriendRequest(String username, String friendUsername) {
        AccountProfile myAccount = this.userList.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findAny()
                    .orElse(null);
        
        myAccount.declineFriendRequest(friendUsername);
    }
    
    public List<AccountProfile> getUserList() {
        return this.userList;
    }
    
    public static void main(String[] args) { 
        ChatServer server = new ChatServer();
        server.execute();
    }
}
