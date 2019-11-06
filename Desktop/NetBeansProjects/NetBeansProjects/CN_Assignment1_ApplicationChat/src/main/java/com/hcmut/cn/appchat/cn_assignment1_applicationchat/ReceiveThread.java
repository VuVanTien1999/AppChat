/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import java.net.ServerSocket;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ChatWindow;
import static java.awt.FileDialog.SAVE;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Vu Van Tien
 */
public class ReceiveThread extends Thread{
    private JLabel filename_label;
    private int port;
    private ChatWindow chatWindow;
    private Socket returnSocket;



    ReceiveThread(Socket returnSocket, ChatWindow correspondingChatWindow) {
        this.returnSocket = returnSocket;
        this.filename_label = correspondingChatWindow.getFilenameLabel();
        this.chatWindow = correspondingChatWindow;
    }
    
    public String getFileExtension(File file){
        if (file == null) {
            return "";
        }
        String name = file.getName();
        int i = name.lastIndexOf('.');
        return (i > 0) ? name.substring(i) : "";
    }
    
    public void run() {
        
        while (true) {
                try {
                    
                    
                    ObjectInputStream onIn = new ObjectInputStream(returnSocket.getInputStream());
                    
                    try {
                        File file = (File) onIn.readObject();
                        filename_label.setText("Receiving...");
                        
                        if (file != null) {
                            FileDialog saveFileDialog = new FileDialog(chatWindow);
                            saveFileDialog.setMode(SAVE);
                            saveFileDialog.setVisible(true);
                            String path = saveFileDialog.getDirectory();
                            String filename = saveFileDialog.getFile();
                            File newFile;
                            if(filename.lastIndexOf('.') == -1){
                                newFile = new File(path + filename + getFileExtension(file));
                            }
                            else{
                                newFile = new File(path + filename);
                            };
                            newFile.createNewFile();
                            
                            Path file_get;
                            file_get = Paths.get(file.getPath());
                            byte[] data = Files.readAllBytes(file_get);
                            
                            Files.write(Paths.get(newFile.getPath()), data);
                            if(filename.lastIndexOf('.') == -1){
                                filename_label.setText(filename + getFileExtension(file) + " is saved");
                            } else filename_label.setText(filename + " is saved");
                        }
                        
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
                        filename_label.setText("in catch");
                        break;
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(ReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error when accept");
                }

            break;

        }
        
    }
    
}
