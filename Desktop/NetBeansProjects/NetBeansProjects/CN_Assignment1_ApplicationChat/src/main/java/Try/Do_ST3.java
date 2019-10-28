/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Try;

import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ServerSocketThread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vu Van Tien
 */
public class Do_ST3 {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(6666);
            ServerSocketThread serverSocketThread = new ServerSocketThread(serverSocket);
            
            serverSocketThread.start();
            
            Socket socket12 = new Socket("localhost", 6666);
            Socket socket21 = serverSocketThread.getSocket();
            
            System.out.println("socket12" + socket12.getInetAddress());
            System.out.println("socket21" + socket21.getInetAddress());
            
        } catch (IOException ex) {
            Logger.getLogger(Do_ST3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
}
