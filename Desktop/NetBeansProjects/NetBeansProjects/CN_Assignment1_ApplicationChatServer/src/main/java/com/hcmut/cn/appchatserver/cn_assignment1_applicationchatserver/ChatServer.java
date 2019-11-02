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
    
    private List<AccountProfile> readFromAccountList() {
        // File contains info of all accounts which are signed up through server
        // Each account info is stored in file by two lines
        // First line is all about account info: "username" "," "password" "," displayed name" "," "number of friend"
        // Second line is the list of friends: "username of friend 1" "," "username of friend 2" "," ...
        String fileName = "AccountList.txt";
        String accountLine = null, friendsLine = null;

        List<AccountProfile> userList = new ArrayList<AccountProfile>();
        
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            // First two lines of file is the header
            accountLine = bufferedReader.readLine();
            friendsLine = bufferedReader.readLine();
            
            while((accountLine = bufferedReader.readLine()) != null) {             
                String[] parts = accountLine.split(",", 4);
                
                friendsLine = bufferedReader.readLine();   
                String[] friends = friendsLine.split(",", Integer.valueOf(parts[3])); // parts[3] is the number of friend
                
                userList.add(new AccountProfile(parts[0], parts[1], parts[2], Integer.valueOf(parts[3]), friends));
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

    private void writeToAccountList(String username, String newFriendUsername) {
        File fileName = new File("Accountlist.txt");
        
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(fileName.toPath(), StandardCharsets.UTF_8));
        
            for (int i = 0; i < fileContent.size(); i += 2) {
                String parts[] = fileContent.get(i).split(",", 2);
                if (parts[0].equals(username)) {
                    String friendUpdate = String.join(",", fileContent.get(i + 1), newFriendUsername);
                    fileContent.set(i + 1, friendUpdate);               
                }
            }
            
            Files.write(fileName.toPath(), fileContent, StandardCharsets.UTF_8);
        } catch(IOException ex) {
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
    
    public boolean isAccountValid(String username, String password, String host, int port) {
        System.out.println(username + password);
        AccountProfile validAccount = this.userList.stream()
                    .filter(user -> user.isAccountValid(username, password))
                    .findAny()
                    .orElse(null);
        System.out.println(username + password);
        if (validAccount != null) {
            validAccount.setActiveStatus(true);
            validAccount.setHost(host);
            validAccount.setPort(port);
            return true;
        }
        return false;
    }
    
    public List<AccountProfile> getUserList() {
        return this.userList;
    }
    
    public static void main(String[] args) { 
        ChatServer server = new ChatServer();
        server.execute();
    }
}
