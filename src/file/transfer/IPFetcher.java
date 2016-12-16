/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.transfer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author kaushal28
 */
public class IPFetcher {
    
    String selfIPAddress; 
    InetAddress currentPingAddr;
    int LoopCurrentIP = 0;
    ArrayList<InetAddress> availableIP = new ArrayList<>();
    
    public ArrayList<InetAddress> fetchIPAddress(){
       
        selfIPAddress = fetchSelfIP();
        
        String[] myIPArray = selfIPAddress.split("\\.");
        
        for (int i = 0; i <= 255; i++) {
            try {
                // build the next IP address
                currentPingAddr = InetAddress.getByName(myIPArray[0] + "." +
                        myIPArray[1] + "." +
                        myIPArray[2] + "." +
                        Integer.toString(LoopCurrentIP));
                // 50ms Timeout for the "ping"
                if (currentPingAddr.isReachable(50)) {
                    availableIP.add(currentPingAddr);
                }
            }
            catch (UnknownHostException e) {
                System.out.println(e.toString());
            }
            catch (IOException e) {
                System.out.println(e.toString());
            }
            LoopCurrentIP++;
        }        
        
        
        return availableIP;
    }
    
    public String fetchSelfIP(){
        String selfIP="";
        boolean flag = true;
        try{
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements()){
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()){
                    InetAddress i = (InetAddress) ee.nextElement();
                    String tempIP = i.getHostAddress();
                    if(tempIP.contains("192.168.")){
                        selfIP = tempIP;
                   //     System.out.println(tempIP);
                        flag = false;
                        break;
                    }
                    
                }
                if(!flag){
                    break;
                }
            }
        }
        catch(SocketException e){
            System.out.println(e.toString());
        }
        return selfIP;
    }
}
