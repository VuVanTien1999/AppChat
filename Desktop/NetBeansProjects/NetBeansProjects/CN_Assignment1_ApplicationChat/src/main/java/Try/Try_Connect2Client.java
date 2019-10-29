/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Try;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ClientInfo;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ChatClient;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ChatWindow;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.Connect2ChatClient;


/**
 *
 * @author Vu Van Tien
 */
public class Try_Connect2Client {
    public static void main(String[] args) {
        ClientInfo client1Info = new ClientInfo("localhost", 5001);
        ClientInfo client2Info = new ClientInfo("localhost", 5002);
        ClientInfo serverInfo = new ClientInfo("localhost", 9999);
        
        ChatClient client1 = new ChatClient(serverInfo, client1Info);
        ChatClient client2 = new ChatClient(serverInfo, client2Info);
        
        if (client1.acceptConnect() && client2.acceptConnect()) {
            Connect2ChatClient connect2ChatClient = new Connect2ChatClient(client1, client2);
            
            ChatWindow chatWindow1 = 
                    new ChatWindow(connect2ChatClient.getServerSocket1(),
                            connect2ChatClient.getSocket12(),
                            connect2ChatClient.getSocket12()
                    );
            ChatWindow chatWindow2 = 
                    //new ChatWindow(connect2ChatClient.getServerSocket2(),connect2ChatClient.getSocket21Accept(), "Client2");
                    new ChatWindow(connect2ChatClient.getServerSocket2(),
                            connect2ChatClient.getSocket21Accept(),
                            connect2ChatClient.getSocket21()
                    );
        }

        
    }
}
