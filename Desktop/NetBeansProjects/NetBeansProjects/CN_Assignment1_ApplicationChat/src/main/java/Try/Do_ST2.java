/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Try;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Vu Van Tien
 *//**
 *
 * @author Vu Van Tien
 */
public class Do_ST2 {
    public static void main(String[] args) throws IOException {
        Socket socket1 = new Socket("localhost", 5555);
        System.out.println("Connect 1 success");
        
        Socket socket2 = new Socket("localhost", 5555);
        System.out.println("Connect 2 success");
    }
}
