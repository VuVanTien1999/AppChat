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
    
    private ServerSocket serverSocket;
    

    ReceiveThread(Socket socket, ChatWindow chatWindow) {
        this.filename_label = chatWindow.getFilenameLabel();
        port = socket.getLocalPort();
        this.chatWindow = chatWindow;
    }

    ReceiveThread(ServerSocket serverSocket, ChatWindow aThis) {
        this.filename_label = aThis.getFilenameLabel();
        this.chatWindow = aThis;
        this.serverSocket = serverSocket;
        
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
                    Socket socket = serverSocket.accept();
                    
                    ObjectInputStream onIn = new ObjectInputStream(socket.getInputStream());
                    
                    try {
                        File file = (File) onIn.readObject();
                        filename_label.setText("Receiving");
                        
                        if (file != null) {
                            FileDialog saveFileDialog = new FileDialog(chatWindow);
                            saveFileDialog.setMode(SAVE);
                            saveFileDialog.setVisible(true);
                            String path = saveFileDialog.getDirectory();
                            String filename = saveFileDialog.getFile();
                            File newFile;
                            if(filename.equals(file.getName())){
                                newFile = new File(path + filename);
                            } else newFile = new File(path + filename + getFileExtension(file));
                            newFile.createNewFile();
                            
                            Path file_get;
                            file_get = Paths.get(file.getPath());
                            byte[] data = Files.readAllBytes(file_get);
                            
                            Files.write(Paths.get(newFile.getPath()), data);
                            if(filename.equals(file.getName())){
                                filename_label.setText(file.getName() + " is saved");
                            } else filename_label.setText(filename + getFileExtension(file) + " is saved");
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

            

        }
        
    }
    
}
