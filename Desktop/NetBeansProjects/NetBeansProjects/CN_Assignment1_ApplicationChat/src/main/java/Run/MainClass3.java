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
 * @author nguye
 */
public class MainClass3 {
    public static void main(String[] args) {
        ClientInfo server = new ClientInfo("localhost", 9000);
            ClientInfo thisClient = new ClientInfo("localhost", 9003);

            ChatClient chatClient = new ChatClient(server, thisClient);
            chatClient.setUpConnectionToServer();

            LoginUI login = new LoginUI(chatClient);
            login.execute();

            ListClientUI listClientUI = new ListClientUI(chatClient);
    }
}
