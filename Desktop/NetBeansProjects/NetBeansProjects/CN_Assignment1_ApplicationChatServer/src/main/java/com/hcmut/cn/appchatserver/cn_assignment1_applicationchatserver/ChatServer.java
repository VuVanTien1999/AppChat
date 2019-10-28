/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchatserver.cn_assignment1_applicationchatserver;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;
import javafx.util.*;

/**
 *
 * @author nguye
 */

public class ChatServer {
    public final static int PORT = 9000;
    
    private List<AccountProfile> userList = new ArrayList<AccountProfile>();
    private Set<UserThread> userThreads = new HashSet<>();
 
    public ChatServer() {}
    
    public void execute() {
        this.userList = this.readFromAccountList();
        
        try (ServerSocket serverSocket = new ServerSocket(this.PORT)) {
 
            System.out.println("Chat Server is listening on port " + this.PORT);
 
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
                              
                UserThread newUser = new UserThread(this, socket);
                userThreads.add(newUser);
                newUser.start();
            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private List<AccountProfile> readFromAccountList() {
        String fileName = "AccountList.txt";
        String line = null;

        List<AccountProfile> userList = new ArrayList<AccountProfile>();
        
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            line = bufferedReader.readLine();
            
            while((line = bufferedReader.readLine()) != null) {                
                String[] parts = line.split(",", 3);
                
                userList.add(new AccountProfile(parts[0], parts[1], parts[2]));
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
    
    private void writeToAccountList(String username, String password, String displayedName) {
        String fileName = "AccountList.txt";

        try {
            FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                                    
            bufferedWriter.newLine();
            bufferedWriter.write(username + "," + password + "," + displayedName);

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
        
    private void refreshAccountList() {
        this.userList = this.readFromAccountList();
    }
    
    public boolean isAccountExisted(String username, String password, String displayedName) {
        AccountProfile existedAccount = this.userList.stream()
                .filter(user -> user.isAccountExisted(username))
                .findAny()
                .orElse(null);
        
        if (existedAccount == null) {
            writeToAccountList(username, password, displayedName);
            this.refreshAccountList();
            return false;
        }
        return true;
    }
    
    public boolean isAccountValid(String username, String password) {
        AccountProfile validAccount = this.userList.stream()
                    .filter(user -> user.isAccountValid(username, password))
                    .findAny()
                    .orElse(null);
        
        if (validAccount != null) {
            validAccount.setActiveStatus(true);
            return true;
        }
        return false;
    }
    
    // Main tasks
    public List<AccountProfile> getUserProfile() {
        List<AccountProfile> activeUser = new ArrayList<AccountProfile>();
        
        activeUser = this.userList;
        
        return activeUser;
        
        //return this.userList.stream().filter(user -> user.getActiveStatus()).collect(Collectors.toList());
    }
    
    public Pair<String, Integer> requestToClient(String requestingUser, String requestedUser) {
        UserThread requestedUserThread = this.userThreads.stream()
                .filter(thread -> thread.getUsernameOwningThread().equals(requestedUser))
                .findAny()
                .orElse(null);
        
        if (requestedUserThread == null) return new Pair<String, Integer>("", 0);
        else {
            AccountProfile requestedUserInfo = this.userList.stream()
                    .filter(user -> user.getUsername().equals(requestedUser))
                    .findAny()
                    .orElse(null);
            
            if (requestedUserInfo == null) return new Pair<String, Integer>("", 0);
            else return new Pair<String, Integer>(requestedUserInfo.getIP(), requestedUserInfo.getPort());
        }         
    }
    
    public static void main(String[] args) { 
        ChatServer server = new ChatServer();
        server.execute();
    }
}
