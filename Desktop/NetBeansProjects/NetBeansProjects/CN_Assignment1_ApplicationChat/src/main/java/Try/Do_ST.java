/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Try;
import java.awt.FileDialog;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ChatWindow;
import java.io.File;
import java.awt.Desktop;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vu Van Tien
 */
public class Do_ST {
    public static void main(String[] args) {
        
        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            
            serverSocket.accept();
            
            serverSocket.close();
            serverSocket = null;
            System.gc();
            System.out.println("Accept 1 success");
            
            
            ServerSocket serverSocket1 = new ServerSocket(5555);
            System.out.println("Line 32: create srSocket1 success");
            serverSocket1.accept();
            System.out.println("Line 34: Accept success");
            serverSocket1.close();
            System.out.println("Accept 2 success");
        } catch (IOException ex) {
            Logger.getLogger(Do_ST.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}
