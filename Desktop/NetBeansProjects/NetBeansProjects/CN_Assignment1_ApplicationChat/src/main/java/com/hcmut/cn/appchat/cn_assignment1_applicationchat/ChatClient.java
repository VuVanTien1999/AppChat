/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

import java.io.*;
import java.net.*;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ClientInfo;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ReadClientThread;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.WriteClientThread;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

/**
 *
 * @author nguye
 */
public class ChatClient {
    private ClientInfo serverInfo;
    private ClientInfo myInfo;
    private ServerSocket myServerSocket;
    private Socket mySocket;
    private String accountInfo;
    private DataInputStream toClient;
    private DataOutputStream fromClient;
    private static List<ClientInfo> listClient = new ArrayList<ClientInfo>();
        
    public ChatClient(ClientInfo _serverInfo, ClientInfo _myInfo) {
        serverInfo = _serverInfo;
        myInfo = _myInfo;
        
    }
    
    
    
    public void setUpConnectionToServer() {
        try {
            Socket socket = new Socket(this.serverInfo.getIP(), this.serverInfo.getPort());
            System.out.println("Connected to the chat server");
            
            try {
                toClient = new DataInputStream(socket.getInputStream());
                fromClient = new DataOutputStream(socket.getOutputStream());
                
//                fromClient.writeUTF(getIP()); // Check THIS
//                fromClient.writeUTF(String.valueOf(getPort())); // Check THIS
            } catch (IOException ex) {
                System.out.println("Error getting input stream: " + ex.getMessage());
                ex.printStackTrace();
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }
    
    public boolean verifyAccount(String username, char[] password) {
        String response = "";

        try {
            System.out.print(username + " " + new String(password));

            fromClient.writeUTF("Verify account");
            fromClient.writeUTF(username);
            fromClient.writeUTF(new String(password));
                       
            response = toClient.readUTF();
            System.out.print(response);            
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        if(response.equals("true")) return true;
        else return false;
    }
    
    public boolean createAccount(String username, char[] password, String displayedName) {
        String response = "";
        
        try {
            System.out.print(username + " " + new String(password) + " " + displayedName);

            fromClient.writeUTF("Create account");
            fromClient.writeUTF(username);
            fromClient.writeUTF(new String(password));
            fromClient.writeUTF(displayedName);
           
            response = toClient.readUTF();
            
            System.out.print(response);
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        if(response.equals("true")) return true;
        else return false;
    }
    
    public void getClientList() {
        try {
            fromClient.writeUTF("Get list of client");
            
            String clientUsername, clientDisplayedname, clientHost;
            int numOfClient = Integer.valueOf(toClient.readUTF());
            int clientPort;
            
            for (int i = 0; i < numOfClient; i++) {
                clientHost = toClient.readUTF();
                clientPort = Integer.valueOf(toClient.readUTF());
                
                listClient.add(new ClientInfo(clientHost, clientPort));
            }
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void listActiveUsers(Set<ClientInfo> listClient) {
        ListClientUI list = new ListClientUI(this.listClient);
        list.setVisible(true);
        
    }
    
    public Socket accept() {
        try {
            myServerSocket = new ServerSocket(myInfo.getPort());
            Socket tempSocket = myServerSocket.accept();
            
            myServerSocket.close();
            
            
            return tempSocket;
            
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            
            if (myServerSocket != null) {
                try {
                    myServerSocket.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex1);
                    ex.printStackTrace();
                }
            }
            
            return null;
        }

    }
    
    public Socket connectSocket(ClientInfo otherClient) {
        try {
            mySocket = new Socket(otherClient.getIP(), otherClient.getPort());
        } catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
        }
        return mySocket;
    }
    
    public static void main(String[] args) {
        
        ClientInfo server = new ClientInfo("localhost", 9000);
        ClientInfo client = new ClientInfo("192.168.18.10", 9999); //???
        
        ChatClient myClient = new ChatClient(server,client);  
        myClient.setUpConnectionToServer();  
               
        ChatApplicationSigninUI clientSigninUI = new ChatApplicationSigninUI();
        clientSigninUI.setVisible(true);

        ChatApplicationSignupUI clientSignupUI;

        while(true) {
            synchronized(clientSigninUI) {
                try {
                    clientSigninUI.wait();
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }

            if (clientSigninUI.isSignin()) {
                if (!myClient.verifyAccount(clientSigninUI.getUsernameSignin(), 
                        clientSigninUI.getPasswordSignin())) {
                    clientSigninUI.label.setText("Wrong username or password.");
                    clientSigninUI.hideAccountInfo();
                }
                else {
                    clientSigninUI.setVisible(false);
                    break;
                }
            }
            else {
                clientSignupUI = new ChatApplicationSignupUI();
                clientSignupUI.setVisible(true);
                clientSigninUI.setVisible(false);
                
                while(true) {
                    synchronized(clientSignupUI) {
                        try {
                            clientSignupUI.wait();
                        }
                        catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }

                    if (myClient.createAccount(clientSignupUI.getUsernameSignup(), 
                            clientSignupUI.getPasswordSignup(), 
                            clientSignupUI.getDisplayednaemSignup())) break;
                    else {
                        
                        clientSignupUI.label.setText("Create acocunt failed. Try again.");
                    } 
                }
                
                clientSigninUI.setVisible(true);
                clientSignupUI.setVisible(false);
                clientSigninUI.label.setText("Re-enter username and password to login.");
            }
        }
        
        myClient.getClientList();
        
        
    }

    int getMyPort() {
        return myInfo.getPort();
    }

    public boolean acceptConnect() {
        return true;
    }

    String getMyIP() {
        return myInfo.getIP();
    }

    ClientInfo getClientInfo() {
        return myInfo;
    }
}
