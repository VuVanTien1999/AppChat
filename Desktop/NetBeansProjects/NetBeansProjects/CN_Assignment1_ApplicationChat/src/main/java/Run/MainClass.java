/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Run;
import com.hcmut.cn.appchat.cn_assignment1_applicationchat.*;
/**
 *
 * @author Vu Van Tien
 */
public class MainClass {
    public static void main(String[] args) {
        ClientInfo server = new ClientInfo("localhost", 9000);
        ClientInfo client = new ClientInfo("localhost", 9001);
        
        ChatClient chatClient = new ChatClient(server, client);
        chatClient.setUpConnectionToServer();
        
        // class LogInUI
        ChatApplicationSigninUI signInUI = new ChatApplicationSigninUI();
        ChatApplicationSignupUI signUpUI;
        
        
        // logInUI.start() 
        
        ListClientUI listClientUI;
        
        // chatWindow.start()
        
        // listClientUI.listen()
        
        
    }
}
