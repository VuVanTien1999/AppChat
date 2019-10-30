/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Run;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.*;
import java.util.List;
import java.util.Timer;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ClientInfo;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.ListClientUI;
import java.util.ArrayList;

/**
 *
 * @author Vu Van Tien
 */
public class MainClass {
    public static void main(String[] args) {
        ClientInfo server = new ClientInfo("localhost", 9000);
        ClientInfo thisClient = new ClientInfo("localhost", 9001);
        
        ChatClient chatClient = new ChatClient(server, thisClient);
        chatClient.setUpConnectionToServer();
        
        LoginUI login = new LoginUI(chatClient);
        login.execute();
        
        List<ClientInfo> listAccount = new ArrayList<ClientInfo>();
        listAccount = chatClient.getClientList();
        
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
