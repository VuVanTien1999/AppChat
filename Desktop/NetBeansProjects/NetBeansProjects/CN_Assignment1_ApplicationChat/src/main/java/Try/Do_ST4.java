/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Try;

import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ClientInfo;
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
public class Do_ST4 {
    public static void main(String[] args) {
        ClientInfo info1 = new ClientInfo("localhost", 5001);
        ClientInfo info2 = new ClientInfo("localhost", 5002);
        
        try {
            ServerSocketThread serverSocketThread= new ServerSocketThread(new ServerSocket(info1.getPort()));
            serverSocketThread.start();
            
            Socket socket = new Socket(info1.getHost(), info1.getPort());
            
        } catch (IOException ex) {
            Logger.getLogger(Do_ST4.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
