/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.transfer;

import java.awt.Dimension;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author kaushal28
 */
public class FileTransfer {

    /**
     * @param args the command line arguments
     */
    
    static ArrayList<InetAddress> availableIPs;
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        Scanner in = new Scanner(System.in);
        int choise = in.nextInt();
        
        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.add(new MainPanel());
                frame.setSize(new Dimension(700,700));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                
                
                
            }
             
        });
        

        switch(choise){
            case 1:
                System.out.println("Fetching all Available IP addresses:");
                Thread thread = new Thread(new Runnable(){
                    
                    @Override
                    public void run(){
                        availableIPs = new IPFetcher().fetchIPAddress();
                    }
                    
                });
                thread.start();
                try{
                    thread.join();
                }
                catch(InterruptedException e){
                    System.out.println(e.toString());
                }
                
                
                
                for(int i = 0;i<availableIPs.size();i++){
                    System.out.println(availableIPs.get(i));
                }
                
                System.out.println("Enter the IP to which you want to send:");
                
                String desired = in.next();
                
                new Thread(new Runnable(){

                    @Override
        	    public void run() {
                	new sender().sendProcess(desired);
                    }
					
        	}).start();
                break;
            
            case 2:
                new Thread(new Runnable(){

                    @Override
        	    public void run() {
                	new receiver().receiveProcess();
                    }
					
        	}).start();
                break;                
                
            default:
                System.out.println("Invalid choise!");
        }
        
    }
    
}
