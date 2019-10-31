/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Run;

import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ChatClient;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ClientInfo;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ListClientUI;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.LoginUI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author talaq
 */
public class MainClass2 {
    public static void main(String[] args) {
        ClientInfo server = new ClientInfo("localhost", 9000);
        ClientInfo thisClient = new ClientInfo("localhost", 9002);
        
        ChatClient chatClient = new ChatClient(server, thisClient);
        chatClient.setUpConnectionToServer();
        
        LoginUI login = new LoginUI(chatClient);
        login.execute();
          
        ListClientUI listClientUI = new ListClientUI(chatClient);
        
        // logInUI.start() 
        
//        List<ClientInfo> list = new ArrayList<ClientInfo>();
//        ClientInfo clientRequest = new ClientInfo("localhohst", 5001);
//        ClientInfo clientTemp = new ClientInfo("localhohst", 5002);
//        list.add(clientRequest);
//        list.add(clientTemp);
//        
//        ListClientUI listClientUI = new ListClientUI(chatClient, list);
//        listClientUI.listen(thisClient.getPort()); // accept
        
        
        
        
        // chatWindow.start()
        
        // listClientUI.listen()
        
        
    }
}
