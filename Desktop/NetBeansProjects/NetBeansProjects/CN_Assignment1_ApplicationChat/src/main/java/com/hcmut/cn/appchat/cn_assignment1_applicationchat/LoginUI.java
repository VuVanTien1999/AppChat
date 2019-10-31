/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.cn.appchat.cn_assignment1_applicationchat;

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
                if (!client.verifyAccount(signin.getUsernameSignin(), signin.getPasswordSignin())) {
                    signin.setLabelNoti("Wrong username or password.");
                    signin.hideAccountInfo();
                }
                else {
                    signin.setVisible(false);
                    break;
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
                            signin.setLabelNoti("Re-enter username and password to login.");
                            break;
                        }
                        else {
                            signup.label.setText("Create acocunt failed. Try again.");
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
