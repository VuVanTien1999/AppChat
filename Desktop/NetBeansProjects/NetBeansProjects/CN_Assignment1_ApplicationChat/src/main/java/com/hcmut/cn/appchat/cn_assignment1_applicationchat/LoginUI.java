/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

import javafx.util.Pair;

/**
 *
 * @author nguye
 */
public class LoginUI {
    ChatClient client;
    SigninUI signin;    
    SignupUI signup;
    
    public LoginUI(ChatClient client) {
        this.client = client;
        this.signin = new SigninUI();
    }
    
    public void execute() {
        while(true) {
            synchronized(this.signin) {
                try {
                    this.signin.wait();
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }

            if (signin.isSignin()) {
                Pair<String, String> result = client.verifyAccount(signin.getUsernameSignin(), signin.getPasswordSignin());
                
                if (result.getKey().equals("false")) {
                    signin.setLabelNoti(result.getValue());
                    signin.hideAccountInfo();
                }
                else {
                    if (result.getValue().equals("Login somewhere else")) {
                        signin.setLabelNoti("Login somewhere else");
                        signin.hideAccountInfo();
                    }
                    else {
                        signin.setVisible(false);
                        break;
                    }
                }
            }
            else {
                signup = new SignupUI();
                signin.setVisible(false);
                
                while(true) {
                    synchronized(signup) {
                        try {
                            signup.wait();
                        }
                        catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    
                    if (signup.isSignup()) {
                        if (client.createAccount(signup.getUsernameSignup(), signup.getPasswordSignup(), signup.getDisplayednaemSignup())) {
                            signin.setVisible(true);
                            signup.setVisible(false);                            
                            signin.setLabelNoti("Re-enter username and password to login");
                            break;
                        }
                        else {
                            signup.label.setText("Username has been used. Try with another one");
                        }
                    }
                    else {
                        signin.setVisible(true);
                        signup.setVisible(false);
                        break;
                    }
                }
                
            }
        }
    }
}
