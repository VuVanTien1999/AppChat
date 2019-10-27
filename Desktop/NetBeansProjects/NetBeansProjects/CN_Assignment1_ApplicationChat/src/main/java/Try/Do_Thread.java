/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Try;

/**
 *
 * @author Vu Van Tien
 */
public class Do_Thread extends Thread {
    private String str = null;
    
    public void run() {
        this.getString();
    }
    
    public void setString(String str) {
        this.str = str;
    }
    
    public void getString() {
        System.out.println(str);
    }
}
